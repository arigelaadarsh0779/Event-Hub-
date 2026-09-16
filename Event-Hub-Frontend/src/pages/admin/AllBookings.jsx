import { useEffect, useState } from "react";
import { getAllBookings } from "../../services/adminService";
import Loading from "../../components/Loading";

const AllBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState("ALL");
  const [search, setSearch] = useState("");

  const loadBookings = () => {
    setLoading(true);
    getAllBookings()
      .then((res) => {
        setBookings(res.data);
        setError(null);
      })
      .catch((err) => {
        setError(err.response?.data?.message || err.response?.data || "Failed to load bookings");
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const formatDate = (dateStr) => {
    if (!dateStr) return "N/A";
    return new Date(dateStr).toLocaleString("en-IN", {
      day: "numeric", month: "short", year: "numeric",
      hour: "2-digit", minute: "2-digit",
    });
  };

  const filtered = bookings.filter((b) => {
    if (filter !== "ALL" && b.status !== filter) return false;
    if (search.trim()) {
      const q = search.toLowerCase();
      return (
        b.BookingNumber?.toLowerCase().includes(q) ||
        b.name?.toLowerCase().includes(q) ||
        String(b.bookingid).includes(q) ||
        String(b.eventId).includes(q)
      );
    }
    return true;
  });

  const totalRevenue = bookings
    .filter((b) => b.status === "CONFIRMED")
    .reduce((sum, b) => sum + (b.totalAmount || 0), 0);

  const confirmedCount = bookings.filter((b) => b.status === "CONFIRMED").length;
  const pendingCount = bookings.filter((b) => b.status === "PENDING").length;

  if (loading) return <div className="page"><Loading message="Loading platform bookings..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="admin-header-flex">
          <div>
            <span className="admin-badge-pill">🛡️ Admin Dashboard</span>
            <h1 className="page-title" style={{ marginTop: "0.5rem" }}>All Platform Bookings</h1>
            <p className="page-subtitle">Monitor and review all ticket orders placed across Event Hub</p>
          </div>
          <button className="btn btn-outline" onClick={loadBookings}>🔄 Refresh</button>
        </div>

        {/* Stats Row */}
        <div className="admin-stats-grid">
          <div className="admin-stat-card">
            <span className="admin-stat-label">Total Bookings</span>
            <span className="admin-stat-value">{bookings.length}</span>
          </div>
          <div className="admin-stat-card">
            <span className="admin-stat-label">Confirmed Bookings</span>
            <span className="admin-stat-value" style={{ color: "#10b981" }}>{confirmedCount}</span>
          </div>
          <div className="admin-stat-card">
            <span className="admin-stat-label">Pending Bookings</span>
            <span className="admin-stat-value" style={{ color: "#f59e0b" }}>{pendingCount}</span>
          </div>
          <div className="admin-stat-card">
            <span className="admin-stat-label">Confirmed Revenue</span>
            <span className="admin-stat-value" style={{ color: "#38bdf8" }}>₹{totalRevenue.toLocaleString("en-IN")}</span>
          </div>
        </div>

        {error ? (
          <div className="error-box">
            <p>⚠️ {error}</p>
            <button className="btn btn-primary" onClick={loadBookings}>Retry</button>
          </div>
        ) : (
          <>
            {/* Toolbar */}
            <div className="admin-toolbar">
              <div className="filter-tabs" style={{ margin: 0, border: "none", padding: 0 }}>
                {["ALL", "CONFIRMED", "PENDING", "CANCELLED"].map((t) => (
                  <button
                    key={t}
                    className={`filter-tab ${filter === t ? "filter-tab-active" : ""}`}
                    onClick={() => setFilter(t)}
                  >
                    {t}
                  </button>
                ))}
              </div>
              <div className="search-wrapper" style={{ width: "300px" }}>
                <span className="search-icon">🔍</span>
                <input
                  type="text"
                  className="search-input"
                  placeholder="Search user, booking #..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                />
              </div>
            </div>

            {filtered.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">📋</div>
                <h3>No bookings found</h3>
                <p>No bookings match the selected criteria.</p>
              </div>
            ) : (
              <div className="admin-table-container">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>Booking #</th>
                      <th>User</th>
                      <th>Event ID</th>
                      <th>Seats</th>
                      <th>Amount</th>
                      <th>Status</th>
                      <th>Booking Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filtered.map((b) => (
                      <tr key={b.bookingid}>
                        <td>
                          <div className="table-meta-cell">
                            <strong className="booking-number">{b.BookingNumber}</strong>
                            <span className="table-subtext">ID: #{b.bookingid}</span>
                          </div>
                        </td>
                        <td>
                          <span style={{ fontWeight: 600, color: "#f8fafc" }}>👤 {b.name}</span>
                        </td>
                        <td>
                          <span className="table-id">Event #{b.eventId}</span>
                        </td>
                        <td>
                          <strong>{b.numberOfSeats}</strong> seat{b.numberOfSeats !== 1 ? "s" : ""}
                        </td>
                        <td className="table-price">₹{Number(b.totalAmount || 0).toLocaleString("en-IN")}</td>
                        <td>
                          <span className={`badge ${
                            b.status === "CONFIRMED" ? "badge-confirmed" :
                            b.status === "CANCELLED" ? "badge-cancelled" : "badge-pending"
                          }`}>
                            {b.status === "CONFIRMED" ? "✅" : b.status === "CANCELLED" ? "❌" : "⏳"} {b.status}
                          </span>
                        </td>
                        <td className="table-date">{formatDate(b.bookingDate)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default AllBookings;
