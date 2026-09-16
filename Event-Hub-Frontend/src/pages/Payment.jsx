import { useEffect, useState } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import { createOrder, verifyPayment } from "../services/paymentService";
import Loading from "../components/Loading";
import toast from "react-hot-toast";

// Loads Razorpay Checkout.js script dynamically
const loadRazorpayScript = () =>
  new Promise((resolve) => {
    if (window.Razorpay) {
      resolve(true);
      return;
    }
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });

const Payment = () => {
  const { bookingId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const booking = location.state?.booking;
  const event = location.state?.event;

  const [loading, setLoading] = useState(false);
  const [orderCreating, setOrderCreating] = useState(false);
  const [paymentData, setPaymentData] = useState(null);
  const [paymentError, setPaymentError] = useState(null);
  const [cancelled, setCancelled] = useState(false);

  // On mount: create the Razorpay order
  useEffect(() => {
    if (!bookingId) {
      setPaymentError("Invalid booking. Please try booking again.");
      return;
    }
    createRazorpayOrder();
  }, [bookingId]);

  const createRazorpayOrder = async () => {
    setOrderCreating(true);
    setPaymentError(null);
    try {
      const res = await createOrder(Number(bookingId));
      setPaymentData(res.data);
    } catch (err) {
      const msg = err.response?.data || "Failed to create payment order. Please try again.";
      setPaymentError(String(msg));
    } finally {
      setOrderCreating(false);
    }
  };

  const handlePayNow = async () => {
    if (!paymentData) return;

    const scriptLoaded = await loadRazorpayScript();
    if (!scriptLoaded) {
      toast.error("Razorpay failed to load. Check your internet connection.");
      return;
    }

    setLoading(true);
    setCancelled(false);

    const options = {
      key: paymentData.keyId,                    // Razorpay Test/Live key from backend
      amount: Math.round(paymentData.amount * 100), // Convert to paise (Razorpay expects paise)
      currency: paymentData.currency,
      name: "Event Hub",
      description: "Event Ticket Booking",
      image: "",
      order_id: paymentData.orderId,             // The Razorpay order ID from backend
      theme: { color: "#6366f1" },

      // ── SUCCESS HANDLER ─────────────────────────────────────────────────
      // Razorpay calls this when payment is successful on their side.
      // We then send the payment details to OUR backend for verification.
      handler: async (response) => {
        try {
          // Send to backend for HMAC signature verification
          await verifyPayment({
            razorpayPaymentId: response.razorpay_payment_id,
            razorpayOrderId: response.razorpay_order_id,
            razorpaySignature: response.razorpay_signature,
          });

          // Payment verified by backend! Navigate to success page.
          toast.success("Payment verified! Booking confirmed! 🎉");
          navigate("/payment-success", {
            state: {
              booking,
              event,
              paymentId: response.razorpay_payment_id,
            },
            replace: true,
          });
        } catch (err) {
          toast.error("Payment verification failed. Please contact support.");
          setPaymentError("Backend payment verification failed.");
          setLoading(false);
        }
      },

      prefill: {
        name: booking?.name || "",
        email: "",
        contact: "",
      },

      // ── CLOSE/CANCEL HANDLER ─────────────────────────────────────────────
      modal: {
        ondismiss: () => {
          setCancelled(true);
          setLoading(false);
          toast("Payment cancelled. You can try again.", { icon: "⚠️" });
        },
      },
    };

    const razorpay = new window.Razorpay(options);

    razorpay.on("payment.failed", (response) => {
      toast.error(`Payment failed: ${response.error.description}`);
      setPaymentError(`Payment failed: ${response.error.description}`);
      setLoading(false);
    });

    razorpay.open();
  };

  if (orderCreating) return <div className="page"><Loading message="Creating payment order..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="payment-page-layout">
          {/* Order Summary */}
          <div className="payment-summary-card">
            <h2 className="payment-summary-title">Order Summary</h2>
            {event && (
              <div className="payment-event-info">
                <span className="event-theme-badge">{event.ThemeOfTheProject || "Event"}</span>
                <h3>{event.Title}</h3>
                <p>📍 {event.Venue}</p>
                <p>📅 {event.Date}</p>
              </div>
            )}
            {booking && (
              <div className="payment-booking-details">
                <div className="summary-row">
                  <span>Booking Number</span>
                  <span>{booking.BookingNumber}</span>
                </div>
                <div className="summary-row">
                  <span>Booking ID</span>
                  <span>#{booking.bookingid}</span>
                </div>
                <div className="summary-row">
                  <span>Number of Seats</span>
                  <span>{booking.numberOfSeats}</span>
                </div>
                <div className="summary-row summary-total">
                  <span>Total Amount</span>
                  <span className="total-amount">Rs.{booking.totalAmount?.toLocaleString("en-IN")}</span>
                </div>
              </div>
            )}
          </div>

          {/* Payment Panel */}
          <div className="payment-panel">
            <div className="payment-panel-header">
              <h2>Complete Payment</h2>
              <p>Secured by Razorpay</p>
            </div>

            {paymentData && (
              <div className="payment-details-box">
                <div className="payment-detail-row">
                  <span>Order ID</span>
                  <span className="order-id-text">{paymentData.orderId}</span>
                </div>
                <div className="payment-detail-row">
                  <span>Amount</span>
                  <span className="amount-text">Rs.{paymentData.amount?.toLocaleString("en-IN")}</span>
                </div>
                <div className="payment-detail-row">
                  <span>Currency</span>
                  <span>{paymentData.currency}</span>
                </div>
                <div className="payment-detail-row">
                  <span>Status</span>
                  <span className="badge-pending">{paymentData.Status}</span>
                </div>
              </div>
            )}

            {paymentError && (
              <div className="error-box">
                <p>⚠️ {paymentError}</p>
                <button className="btn btn-outline" onClick={createRazorpayOrder}>Retry</button>
              </div>
            )}

            {cancelled && (
              <div className="warning-box">
                <p>⚠️ Payment was cancelled or not completed.</p>
                <div className="warning-actions">
                  <button className="btn btn-primary" onClick={handlePayNow}>Try Again</button>
                  <button className="btn btn-outline" onClick={() => navigate(-1)}>Back to Booking</button>
                </div>
              </div>
            )}

            {!paymentError && paymentData && !loading && !cancelled && (
              <button
                className="btn btn-primary btn-full btn-pay"
                onClick={handlePayNow}
                id="pay-now-btn"
              >
                Pay Rs.{paymentData.amount?.toLocaleString("en-IN")} with Razorpay 🔒
              </button>
            )}

            {loading && <Loading message="Processing payment..." />}

            <div className="payment-trust-badges">
              <span>🔒 256-bit SSL</span>
              <span>🏦 Razorpay Secured</span>
              <span>✅ Instant Confirmation</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Payment;
