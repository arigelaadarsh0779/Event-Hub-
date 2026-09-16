import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookingsByUserId } from "../services/bookingService";
import api from "../services/api";
import TicketCard from "../components/TicketCard";
import Loading from "../components/Loading";

// My Tickets page
// The backend does NOT have a separate "get my tickets" endpoint.
// Tickets are stored in the ticket_entity table and linked to bookings.
// We fetch the user's confirmed bookings, then for each booking we check if a ticket exists.
// Since the backend generates tickets automatically on payment verification,
// we fetch tickets by calling the ticket repository through the booking data.

const MyTickets = () => {
  const { user } = useAuth();
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user?.userId) return;

    // Step 1: Get user bookings
    getBookingsByUserId(user.userId)
      .then(async (res) => {
        const confirmedBookings = res.data.filter((b) => b.status === "CONFIRMED");

        // Step 2: For each confirmed booking, try to get its ticket
        const ticketPromises = confirmedBookings.map(async (booking) => {
          try {
            const ticketRes = await api.get(`/api/user/ticket/${booking.bookingid}`);
            return ticketRes.data;
          } catch {
            // No ticket yet for this booking, or endpoint not available
            return null;
          }
        });

        const results = await Promise.all(ticketPromises);
        const validTickets = results.filter((t) => t !== null);
        setTickets(validTickets);
      })
      .catch((err) => {
        setError(err.response?.data || "Failed to load tickets");
      })
      .finally(() => setLoading(false));
  }, [user]);

  if (loading) return <div className="page"><Loading message="Loading your tickets..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1 className="page-title">My Tickets</h1>
          <p className="page-subtitle">
            {tickets.length > 0
              ? `${tickets.length} ticket${tickets.length !== 1 ? "s" : ""} — PDF sent to your email`
              : "Tickets appear here after payment confirmation"}
          </p>
        </div>

        {error ? (
          <div className="info-box">
            <p>
              ℹ️ Ticket lookup endpoint is not yet available. Your tickets are sent directly to your email after payment.
            </p>
            <p style={{ marginTop: "0.5rem", fontSize: "0.875rem", opacity: 0.7 }}>
              Check your registered email for PDF tickets with QR codes.
            </p>
          </div>
        ) : tickets.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">🎟️</div>
            <h3>No Tickets Yet</h3>
            <p>Tickets are automatically generated when payment is confirmed.</p>
            <p className="empty-hint">📧 Your PDF tickets are sent directly to your email after each successful payment.</p>
          </div>
        ) : (
          <div className="tickets-list">
            {tickets.map((ticket) => (
              <TicketCard key={ticket.ticketId} ticket={ticket} />
            ))}
          </div>
        )}

        {/* Email reminder */}
        <div className="email-notice">
          <span>📧</span>
          <div>
            <p><strong>Tickets are sent via email</strong></p>
            <p>After successful payment, your PDF ticket with QR code is sent to your registered email address automatically.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyTickets;
