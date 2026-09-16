// Displays a single ticket card with QR placeholder and Print option
// Ticket data: { ticketId, ticketNumber, bookingId, generatedAt, status }
const TicketCard = ({ ticket }) => {
  const formatDate = (dateStr) => {
    if (!dateStr) return "N/A";
    return new Date(dateStr).toLocaleString("en-IN", {
      day: "numeric", month: "short", year: "numeric",
      hour: "2-digit", minute: "2-digit",
    });
  };

  const handlePrint = () => {
    window.print();
  };

  const statusClass = ticket.status === "VALID" ? "badge-confirmed" : "badge-cancelled";

  return (
    <div className="ticket-card">
      {/* Decorative ticket shape */}
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

      <div className="ticket-right">
        <div className="ticket-header">
          <span className="ticket-number">{ticket.ticketNumber}</span>
          <span className={`badge ${statusClass}`}>{ticket.status}</span>
        </div>

        <div className="ticket-details">
          <div className="ticket-detail-item">
            <span className="detail-label">Ticket ID</span>
            <span className="detail-value">#{ticket.ticketId}</span>
          </div>
          <div className="ticket-detail-item">
            <span className="detail-label">Booking ID</span>
            <span className="detail-value">#{ticket.bookingId}</span>
          </div>
          <div className="ticket-detail-item">
            <span className="detail-label">Generated At</span>
            <span className="detail-value">{formatDate(ticket.generatedAt)}</span>
          </div>
          <div className="ticket-detail-item">
            <span className="detail-label">Status</span>
            <span className="detail-value">{ticket.status}</span>
          </div>
        </div>

        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "0.5rem" }}>
          <div className="ticket-note">
            📧 PDF ticket sent to your email
          </div>
          <button className="btn btn-outline-sm" onClick={handlePrint} title="Print or save as PDF">
            🖨️ Print
          </button>
        </div>
      </div>
    </div>
  );
};

export default TicketCard;
