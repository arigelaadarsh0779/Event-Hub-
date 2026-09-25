import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";
import Loading from "../components/Loading";

const MyTickets = () => {
  const { user } = useAuth();
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user?.userId) return;

    api.get(`/api/user/mytickets/${user.userId}`)
      .then((res) => {
        setTickets(res.data || []);
      })
      .catch((err) => {
        const msg = err.response?.data || "Failed to load tickets";
        setError(String(msg));
      })
      .finally(() => setLoading(false));
  }, [user]);

  const handleDownloadPdf = (bookingId) => {
    const apiBaseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
    window.open(`${apiBaseUrl}/api/tickets/pdf/${bookingId}`, "_blank");
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return "N/A";
    try {
      return new Date(dateStr).toLocaleString("en-IN", {
        day: "numeric", month: "short", year: "numeric",
        hour: "2-digit", minute: "2-digit",
      });
    } catch {
      return dateStr;
    }
  };

  if (loading) return <div className="page"><Loading message="Loading your tickets..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1 className="page-title">My Tickets 🎟️</h1>
          <p className="page-subtitle">
            {tickets.length > 0
              ? `${tickets.length} ticket${tickets.length !== 1 ? "s" : ""} — Download your PDF tickets below`
              : "Your confirmed tickets will appear here"}
          </p>
        </div>

        {error ? (
          <div className="error-box">
            <p>⚠️ {error}</p>
          </div>
        ) : tickets.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">🎟️</div>
            <h3>No Tickets Yet</h3>
            <p>Tickets are automatically generated when payment is confirmed.</p>
            <p className="empty-hint">📧 Your PDF tickets are also sent directly to your email after each successful payment.</p>
          </div>
        ) : (
          <div className="tickets-list">
            {tickets.map((ticket) => (
              <div key={ticket.ticketId} className="ticket-card">
                {/* Left side */}
                <div className="ticket-left">
                  <div className="ticket-icon">🎟️</div>
                  <div className="ticket-qr-placeholder">
                    <div className="qr-box">
                      <span className="qr-label">EVENT PASS</span>
                      <p className="qr-number">{ticket.ticketNumber}</p>
                    </div>
                  </div>
                </div>

                {/* Dashed divider */}
                <div className="ticket-divider">
                  <div className="notch notch-top"></div>
                  <div className="dashed-line"></div>
                  <div className="notch notch-bottom"></div>
                </div>

                {/* Right side */}
                <div className="ticket-right">
                  <div className="ticket-header">
                    <span className="ticket-number">{ticket.ticketNumber}</span>
                    <span className={`badge ${ticket.status === "VALID" ? "badge-confirmed" : "badge-cancelled"}`}>
                      {ticket.status}
                    </span>
                  </div>

                  <div className="ticket-details">
                    {ticket.eventTitle && (
                      <div className="ticket-detail-item">
                        <span className="detail-label">Event</span>
                        <span className="detail-value">{ticket.eventTitle}</span>
                      </div>
                    )}
                    {ticket.eventDate && (
                      <div className="ticket-detail-item">
                        <span className="detail-label">Date</span>
                        <span className="detail-value">📅 {ticket.eventDate}</span>
                      </div>
                    )}
                    {ticket.eventVenue && (
                      <div className="ticket-detail-item">
                        <span className="detail-label">Venue</span>
                        <span className="detail-value">📍 {ticket.eventVenue}</span>
                      </div>
                    )}
                    {ticket.eventTime && (
                      <div className="ticket-detail-item">
                        <span className="detail-label">Time</span>
                        <span className="detail-value">⏰ {ticket.eventTime}</span>
                      </div>
                    )}
                    <div className="ticket-detail-item">
                      <span className="detail-label">Seats</span>
                      <span className="detail-value">{ticket.numberOfSeats}</span>
                    </div>
                    <div className="ticket-detail-item">
                      <span className="detail-label">Booking ID</span>
                      <span className="detail-value">#{ticket.bookingId}</span>
                    </div>
                    <div className="ticket-detail-item">
                      <span className="detail-label">Generated</span>
                      <span className="detail-value">{formatDate(ticket.generatedAt)}</span>
                    </div>
                  </div>

                  <div style={{ display: "flex", gap: "0.6rem", flexWrap: "wrap", marginTop: "0.5rem" }}>
                    <button
                      className="btn btn-primary-sm"
                      onClick={() => handleDownloadPdf(ticket.bookingId)}
                      style={{ flex: 1 }}
                    >
                      📥 Download PDF
                    </button>
                    <button
                      className="btn btn-outline-sm"
                      onClick={() => handleDownloadPdf(ticket.bookingId)}
                      style={{ flex: 1 }}
                    >
                      👁️ View Ticket
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Email reminder */}
        <div className="email-notice">
          <span>📧</span>
          <div>
            <p><strong>Tickets are also sent via email</strong></p>
            <p>After successful payment, your PDF ticket with QR code is sent to your registered email address automatically.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyTickets;
