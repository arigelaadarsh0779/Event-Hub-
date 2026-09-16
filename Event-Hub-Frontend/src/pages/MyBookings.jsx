import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getBookingsByUserId } from "../services/bookingService";
import BookingCard from "../components/BookingCard";
import Loading from "../components/Loading";

const MyBookings = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeFilter, setActiveFilter] = useState("ALL");

  const loadBookings = () => {
    setLoading(true);
    getBookingsByUserId(user.userId)
      .then((res) => setBookings(res.data))
      .catch((err) => {
        const msg = err.response?.data || "Failed to load bookings";
        setError(String(msg));
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    if (user?.userId) loadBookings();
  }, [user]);

  const handleCancelled = (cancelledId) => {
    setBookings((prev) =>
      prev.map((b) =>
        b.bookingid === cancelledId ? { ...b, status: "CANCELLED" } : b
      )
    );
  };

  const filtered = activeFilter === "ALL"
    ? bookings
    : bookings.filter((b) => b.status === activeFilter);

  const counts = {
    ALL: bookings.length,
    CONFIRMED: bookings.filter((b) => b.status === "CONFIRMED").length,
    PENDING: bookings.filter((b) => b.status === "PENDING").length,
    CANCELLED: bookings.filter((b) => b.status === "CANCELLED").length,
  };

  if (loading) return <div className="page"><Loading message="Loading your bookings..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="page-header">
          <h1 className="page-title">My Bookings</h1>
          <p className="page-subtitle">{bookings.length} booking{bookings.length !== 1 ? "s" : ""} total</p>
        </div>

        {error ? (
          <div className="error-box">
            <p>⚠️ {error}</p>
            <button className="btn btn-primary" onClick={loadBookings}>Retry</button>
          </div>
        ) : (
          <>
            {/* Filter tabs */}
            <div className="filter-tabs">
              {["ALL", "CONFIRMED", "PENDING", "CANCELLED"].map((tab) => (
                <button
                  key={tab}
                  className={`filter-tab ${activeFilter === tab ? "filter-tab-active" : ""}`}
                  onClick={() => setActiveFilter(tab)}
                >
                  {tab} <span className="tab-count">{counts[tab]}</span>
                </button>
              ))}
            </div>

            {filtered.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">📋</div>
                <h3>No {activeFilter !== "ALL" ? activeFilter.toLowerCase() : ""} bookings</h3>
                <p>
                  {activeFilter === "ALL"
                    ? "You haven't booked any events yet. Start exploring!"
                    : `No bookings with ${activeFilter} status.`}
                </p>
              </div>
            ) : (
              <div className="bookings-list">
                {filtered.map((booking) => (
                  <BookingCard
                    key={booking.bookingid}
                    booking={booking}
                    onCancelled={handleCancelled}
                  />
                ))}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default MyBookings;
