import { useLocation, useNavigate, Link } from "react-router-dom";
import { useEffect } from "react";

const PaymentSuccess = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const booking = location.state?.booking;
  const event = location.state?.event;
  const paymentId = location.state?.paymentId;

  // If someone navigates here directly without payment state, redirect home
  useEffect(() => {
    if (!booking && !paymentId) {
      navigate("/", { replace: true });
    }
  }, []);

  return (
    <div className="page">
      <div className="container">
        <div className="success-page">
          {/* Success animation */}
          <div className="success-icon-wrapper">
            <div className="success-icon">🎉</div>
            <div className="success-ripple"></div>
          </div>

          <h1 className="success-title">Payment Successful!</h1>
          <p className="success-subtitle">
            Your booking is confirmed and your ticket has been sent to your email.
          </p>

          {/* Booking Details */}
          {booking && (
            <div className="success-details">
              <h2 className="success-details-title">Booking Details</h2>
              <div className="success-detail-grid">
                <div className="success-detail-item">
                  <span className="detail-label">Booking Number</span>
                  <span className="detail-value highlight">{booking.BookingNumber}</span>
                </div>
                <div className="success-detail-item">
                  <span className="detail-label">Booking ID</span>
                  <span className="detail-value">#{booking.bookingid}</span>
                </div>
                <div className="success-detail-item">
                  <span className="detail-label">Number of Seats</span>
                  <span className="detail-value">{booking.numberOfSeats}</span>
                </div>
                <div className="success-detail-item">
                  <span className="detail-label">Amount Paid</span>
                  <span className="detail-value amount">Rs.{booking.totalAmount?.toLocaleString("en-IN")}</span>
                </div>
                <div className="success-detail-item">
                  <span className="detail-label">Status</span>
                  <span className="badge badge-confirmed">✅ CONFIRMED</span>
                </div>
                {paymentId && (
                  <div className="success-detail-item">
                    <span className="detail-label">Payment ID</span>
                    <span className="detail-value payment-id">{paymentId}</span>
                  </div>
                )}
              </div>

              {event && (
                <div className="success-event-info">
                  <h3>Event Details</h3>
                  <p><strong>{event.Title}</strong></p>
                  <p>📍 {event.Venue}</p>
                  <p>📅 {event.Date}</p>
                </div>
              )}
            </div>
          )}

          {/* Email notice */}
          <div className="email-notice">
            <span>📧</span>
            <div>
              <p><strong>Your ticket is on its way!</strong></p>
              <p>A PDF ticket with QR code has been sent to your registered email address.</p>
            </div>
          </div>

          {/* Actions */}
          <div className="success-actions">
            <Link to="/my-bookings" className="btn btn-primary">View My Bookings</Link>
            <Link to="/my-tickets" className="btn btn-secondary">View My Tickets</Link>
            <Link to="/" className="btn btn-outline">Go Home</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PaymentSuccess;
