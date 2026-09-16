import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAllEvents } from "../services/eventService";
import { createBooking } from "../services/bookingService";
import { deleteEvent } from "../services/adminService";
import { useAuth } from "../context/AuthContext";
import Loading from "../components/Loading";
import toast from "react-hot-toast";

const EventDetails = () => {
  const { eventId } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, isAdmin, user } = useAuth();

  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [seats, setSeats] = useState(1);
  const [booking, setBooking] = useState(false);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    getAllEvents()
      .then((res) => {
        const found = res.data.find((e) => String(e.eventid) === String(eventId));
        if (!found) setError("Event not found");
        else setEvent(found);
      })
      .catch(() => setError("Failed to load event details"))
      .finally(() => setLoading(false));
  }, [eventId]);

  const handleBook = async () => {
    if (isAdmin) {
      toast.error("Administrators cannot book tickets. Use a regular user account.");
      return;
    }

    if (!isAuthenticated) {
      toast.error("Please login to book tickets");
      navigate("/login", { state: { from: `/events/${eventId}` } });
      return;
    }

    if (seats < 1 || seats > event.RemainingSeats) {
      toast.error("Invalid number of seats");
      return;
    }

    setBooking(true);
    try {
      const res = await createBooking({
        eventId: event.eventid,
        userId: user.userId,
        numberOfSeats: seats,
      });
      const bookingData = res.data;
      toast.success("Booking created! Proceeding to payment...");
      navigate(`/payment/${bookingData.bookingid}`, {
        state: { booking: bookingData, event },
      });
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || "Booking failed. Please try again.";
      toast.error(String(msg));
    } finally {
      setBooking(false);
    }
  };

  const handleDeleteByAdmin = async () => {
    if (!window.confirm(`Are you sure you want to delete "${event.Title}"? This cannot be undone.`)) {
      return;
    }

    setDeleting(true);
    try {
      await deleteEvent(event.eventid);
      toast.success("Event deleted successfully! 🗑️");
      navigate("/admin/events");
    } catch (err) {
      toast.error(err.response?.data || "Failed to delete event.");
      setDeleting(false);
    }
  };

  if (loading) return <div className="page"><Loading message="Loading event details..." /></div>;
  if (error) return (
    <div className="page"><div className="container"><div className="error-box">⚠️ {error}</div></div></div>
  );
  if (!event) return null;

  const totalAmount = (event.ticketPrice || 0) * seats;
  const soldOut = event.RemainingSeats <= 0;

  const formatDate = (d) => d ? new Date(d).toLocaleDateString("en-IN", { weekday: "long", day: "numeric", month: "long", year: "numeric" }) : "TBA";
  const formatTime = (t) => t ? t.slice(0, 5) : "TBA";
  const seatsPercent = event.TotalSeats ? Math.round(((event.TotalSeats - event.RemainingSeats) / event.TotalSeats) * 100) : 0;

  return (
    <div className="page">
      <div className="container">
        {/* Back button */}
        <button className="back-btn" onClick={() => navigate(-1)}>← Back</button>

        <div className="event-detail-layout">
          {/* Left: Event Info */}
          <div className="event-detail-main">
            <div className="event-detail-header">
              <span className="event-theme-badge-lg">{event.ThemeOfTheProject || "General"}</span>
              <h1 className="event-detail-title">{event.Title}</h1>
              <p className="event-detail-organizer">Organized by <strong>{event.Organizer}</strong></p>
            </div>

            <div className="event-info-grid">
              <div className="event-info-card">
                <span className="info-icon">📅</span>
                <div>
                  <p className="info-label">Date</p>
                  <p className="info-value">{formatDate(event.Date)}</p>
                </div>
              </div>
              <div className="event-info-card">
                <span className="info-icon">🕐</span>
                <div>
                  <p className="info-label">Time</p>
                  <p className="info-value">{formatTime(event.StartTime)} — {formatTime(event.EndTime)}</p>
                </div>
              </div>
              <div className="event-info-card">
                <span className="info-icon">📍</span>
                <div>
                  <p className="info-label">Venue</p>
                  <p className="info-value">{event.Venue}</p>
                </div>
              </div>
              <div className="event-info-card">
                <span className="info-icon">🎭</span>
                <div>
                  <p className="info-label">Theme</p>
                  <p className="info-value">{event.ThemeOfTheProject}</p>
                </div>
              </div>
              <div className="event-info-card">
                <span className="info-icon">💺</span>
                <div>
                  <p className="info-label">Seats Available</p>
                  <p className={`info-value ${event.RemainingSeats < 10 ? "seats-critical" : ""}`}>
                    {event.RemainingSeats} / {event.TotalSeats}
                  </p>
                </div>
              </div>
              <div className="event-info-card">
                <span className="info-icon">💰</span>
                <div>
                  <p className="info-label">Ticket Price</p>
                  <p className="info-value price">₹{Number(event.ticketPrice).toLocaleString("en-IN")}</p>
                </div>
              </div>
            </div>

            {/* Seat availability bar */}
            <div className="availability-section">
              <div className="seats-bar-container">
                <div className="seats-bar-fill" style={{ width: `${seatsPercent}%`, background: event.RemainingSeats < 10 ? "#ef4444" : "#6366f1" }} />
              </div>
              <p className="seats-bar-label">{seatsPercent}% seats booked ({event.RemainingSeats} remaining)</p>
            </div>

            <div className="event-description-section">
              <h2 className="section-heading">About this Event</h2>
              <p className="event-full-description">{event.Description}</p>
            </div>
          </div>

          {/* Right Panel: ADMIN ACTIONS or USER BOOKING */}
          {isAdmin ? (
            <div className="booking-panel">
              <div className="booking-panel-header">
                <span className="admin-badge-pill">🛡️ Admin Mode</span>
                <h2 className="booking-panel-title" style={{ marginTop: "0.5rem" }}>Event Controls</h2>
                <p className="booking-panel-price">
                  ₹{Number(event.ticketPrice).toLocaleString("en-IN")} <span>ticket price</span>
                </p>
              </div>

              <div className="admin-panel-details">
                <div className="summary-row">
                  <span>Event ID</span>
                  <span>#{event.eventid}</span>
                </div>
                <div className="summary-row">
                  <span>Total Capacity</span>
                  <span>{event.TotalSeats} seats</span>
                </div>
                <div className="summary-row">
                  <span>Booked Seats</span>
                  <span>{event.TotalSeats - event.RemainingSeats} seats</span>
                </div>
                <div className="summary-row">
                  <span>Available Seats</span>
                  <span style={{ color: event.RemainingSeats < 10 ? "#ef4444" : "#10b981", fontWeight: 700 }}>
                    {event.RemainingSeats} seats
                  </span>
                </div>
              </div>

              <div style={{ display: "flex", flexDirection: "column", gap: "0.75rem", marginTop: "1.5rem" }}>
                <button
                  className="btn btn-primary btn-full"
                  onClick={() => navigate(`/admin/edit-event/${event.eventid}`)}
                >
                  ✏️ Edit This Event
                </button>
                <button
                  className="btn btn-danger btn-full"
                  onClick={handleDeleteByAdmin}
                  disabled={deleting}
                >
                  {deleting ? "Deleting..." : "🗑️ Delete Event"}
                </button>
                <button
                  className="btn btn-secondary btn-full"
                  onClick={() => navigate("/admin/events")}
                >
                  Back to All Events
                </button>
              </div>

              <p className="booking-note" style={{ marginTop: "1.5rem" }}>
                ℹ️ Admins do not book tickets. You can edit event details or manage inventory from here.
              </p>
            </div>
          ) : (
            <div className="booking-panel">
              <div className="booking-panel-header">
                <h2 className="booking-panel-title">Book Tickets</h2>
                <p className="booking-panel-price">₹{Number(event.ticketPrice).toLocaleString("en-IN")} <span>per seat</span></p>
              </div>

              {soldOut ? (
                <div className="sold-out-badge">🚫 SOLD OUT</div>
              ) : (
                <>
                  <div className="seat-selector">
                    <label className="form-label">Number of Seats</label>
                    <div className="seat-counter">
                      <button
                        className="counter-btn"
                        onClick={() => setSeats((s) => Math.max(1, s - 1))}
                        disabled={seats <= 1}
                        id="seats-minus-btn"
                      >−</button>
                      <span className="counter-value">{seats}</span>
                      <button
                        className="counter-btn"
                        onClick={() => setSeats((s) => Math.min(event.RemainingSeats, s + 1))}
                        disabled={seats >= event.RemainingSeats}
                        id="seats-plus-btn"
                      >+</button>
                    </div>
                    <p className="seat-hint">Max {event.RemainingSeats} seats available</p>
                  </div>

                  <div className="booking-summary">
                    <div className="summary-row">
                      <span>Price per seat</span>
                      <span>₹{Number(event.ticketPrice).toLocaleString("en-IN")}</span>
                    </div>
                    <div className="summary-row">
                      <span>Number of seats</span>
                      <span>{seats}</span>
                    </div>
                    <div className="summary-row summary-total">
                      <span>Total Amount</span>
                      <span className="total-amount">₹{totalAmount.toLocaleString("en-IN")}</span>
                    </div>
                  </div>

                  <button
                    className="btn btn-primary btn-full btn-book"
                    onClick={handleBook}
                    disabled={booking || soldOut}
                    id="book-now-btn"
                  >
                    {booking ? <><span className="btn-spinner"></span>Creating Booking...</> : "Book Now 🎟️"}
                  </button>

                  {!isAuthenticated && (
                    <p className="booking-note">You need to login to book tickets</p>
                  )}
                </>
              )}

              <div className="booking-trust">
                <span>🔒 Secured by Razorpay</span>
                <span>📧 Ticket via Email</span>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default EventDetails;
