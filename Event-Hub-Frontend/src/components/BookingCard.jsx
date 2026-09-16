import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { cancelBooking } from "../services/bookingService";
import toast from "react-hot-toast";

// Displays a single booking with status badge, cancel button, and Pay Now for pending bookings
const BookingCard = ({ booking, onCancelled }) => {
  const [cancelling, setCancelling] = useState(false);
  const navigate = useNavigate();

  const statusClass = {
    CONFIRMED: "badge-confirmed",
    PENDING: "badge-pending",
    CANCELLED: "badge-cancelled",
  }[booking.status] || "badge-pending";

  const statusIcon = {
    CONFIRMED: "✅",
    PENDING: "⏳",
    CANCELLED: "❌",
  }[booking.status] || "⏳";

  const formatDate = (dateStr) => {
    if (!dateStr) return "N/A";
    return new Date(dateStr).toLocaleString("en-IN", {
      day: "numeric", month: "short", year: "numeric",
      hour: "2-digit", minute: "2-digit",
    });
  };

  const handleCancel = async () => {
    if (!window.confirm("Are you sure you want to cancel this booking?")) return;
    setCancelling(true);
    try {
      await cancelBooking(booking.bookingid);
      toast.success("Booking cancelled successfully. Seats have been restored.");
      if (onCancelled) onCancelled(booking.bookingid);
    } catch (err) {
      toast.error(err.response?.data || "Failed to cancel booking");
    } finally {
      setCancelling(false);
    }
  };

  const handleProceedToPayment = () => {
    navigate(`/payment/${booking.bookingid}`, {
      state: { booking },
    });
  };

  return (
    <div className={`booking-card ${booking.status === "CANCELLED" ? "booking-cancelled" : ""}`}>
      <div className="booking-card-header">
        <div>
          <span className="booking-number">{booking.BookingNumber}</span>
          <p className="booking-date">{formatDate(booking.bookingDate)}</p>
        </div>
        <span className={`badge ${statusClass}`}>
          {statusIcon} {booking.status}
        </span>
      </div>

      <div className="booking-card-body">
        <div className="booking-detail">
          <span className="detail-label">Booking ID</span>
          <span className="detail-value">#{booking.bookingid}</span>
        </div>
        <div className="booking-detail">
          <span className="detail-label">Event ID</span>
          <span className="detail-value">#{booking.eventId}</span>
        </div>
        <div className="booking-detail">
          <span className="detail-label">Seats</span>
          <span className="detail-value">{booking.numberOfSeats}</span>
        </div>
        <div className="booking-detail">
          <span className="detail-label">Total Amount</span>
          <span className="detail-value amount">₹{booking.totalAmount?.toLocaleString("en-IN")}</span>
        </div>
        <div className="booking-detail">
          <span className="detail-label">Booked By</span>
          <span className="detail-value">{booking.name}</span>
        </div>
      </div>

      {booking.status !== "CANCELLED" && (
        <div className="booking-card-footer">
          {booking.status === "PENDING" && (
            <button
              className="btn btn-primary-sm"
              onClick={handleProceedToPayment}
              style={{ marginRight: "auto" }}
            >
              Pay Now 💳
            </button>
          )}

          <button
            className="btn btn-danger-sm"
            onClick={handleCancel}
            disabled={cancelling || booking.status === "CONFIRMED"}
            title={booking.status === "CONFIRMED" ? "Confirmed bookings cannot be cancelled" : "Cancel this booking"}
          >
            {cancelling ? "Cancelling..." : "Cancel Booking"}
          </button>

          {booking.status === "CONFIRMED" && (
            <span className="cancel-note">✅ Payment confirmed — ticket sent to email</span>
          )}
        </div>
      )}
    </div>
  );
};

export default BookingCard;
