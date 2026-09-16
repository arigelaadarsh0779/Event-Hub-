import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getAllEvents } from "../../services/eventService";
import { deleteEvent } from "../../services/adminService";
import Loading from "../../components/Loading";
import toast from "react-hot-toast";

const ManageEvents = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const loadEvents = () => {
    setLoading(true);
    getAllEvents()
      .then((res) => {
        setEvents(res.data);
        setError(null);
      })
      .catch((err) => {
        setError(err.response?.data?.message || err.response?.data || "Failed to load events. Please check backend.");
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadEvents();
  }, []);

  const handleDelete = async (eventId, title) => {
    if (!window.confirm(`Are you sure you want to delete the event "${title}"? This cannot be undone.`)) {
      return;
    }

    setDeletingId(eventId);
    try {
      await deleteEvent(eventId);
      toast.success("Event deleted successfully! 🗑️");
      setEvents((prev) => prev.filter((e) => e.eventid !== eventId));
    } catch (err) {
      toast.error(err.response?.data || "Failed to delete event.");
    } finally {
      setDeletingId(null);
    }
  };

  const filtered = events.filter((e) => {
    if (!search.trim()) return true;
    const q = search.toLowerCase();
    return (
      e.Title?.toLowerCase().includes(q) ||
      e.Venue?.toLowerCase().includes(q) ||
      e.ThemeOfTheProject?.toLowerCase().includes(q) ||
      e.Organizer?.toLowerCase().includes(q)
    );
  });

  if (loading) return <div className="page"><Loading message="Loading admin events..." /></div>;

  return (
    <div className="page">
      <div className="container">
        <div className="admin-header-flex">
          <div>
            <span className="admin-badge-pill">🛡️ Admin Dashboard</span>
            <h1 className="page-title" style={{ marginTop: "0.5rem" }}>Manage Events</h1>
            <p className="page-subtitle">View, edit, add, or remove events across the platform</p>
          </div>
          <Link to="/admin/add-event" className="btn btn-primary" id="add-event-btn">
            ➕ Add New Event
          </Link>
        </div>

        {error ? (
          <div className="error-box">
            <p>⚠️ {error}</p>
            <button className="btn btn-primary" onClick={loadEvents}>Retry</button>
          </div>
        ) : (
          <>
            {/* Search & Counter */}
            <div className="admin-toolbar">
              <div className="search-wrapper" style={{ flex: 1, maxWidth: "450px" }}>
                <span className="search-icon">🔍</span>
                <input
                  type="text"
                  className="search-input"
                  placeholder="Search events by title, venue, theme..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                />
                {search && <button className="search-clear" onClick={() => setSearch("")}>✕</button>}
              </div>
              <span className="results-count" style={{ margin: 0 }}>
                Total: <strong>{events.length}</strong> events
              </span>
            </div>

            {filtered.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🎪</div>
                <h3>No events found</h3>
                <p>{events.length === 0 ? "No events have been created yet." : "No events match your search query."}</p>
                <Link to="/admin/add-event" className="btn btn-primary">Create the First Event</Link>
              </div>
            ) : (
              <div className="admin-table-container">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Title & Theme</th>
                      <th>Date & Time</th>
                      <th>Venue</th>
                      <th>Seats (Left/Total)</th>
                      <th>Price</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filtered.map((event) => (
                      <tr key={event.eventid}>
                        <td className="table-id">#{event.eventid}</td>
                        <td>
                          <div className="table-title-cell">
                            <strong className="event-row-title">{event.Title}</strong>
                            <span className="table-theme-tag">{event.ThemeOfTheProject || "General"}</span>
                          </div>
                        </td>
                        <td>
                          <div className="table-meta-cell">
                            <span>📅 {event.Date}</span>
                            <span className="table-subtext">⏰ {event.StartTime?.slice(0, 5)} - {event.EndTime?.slice(0, 5)}</span>
                          </div>
                        </td>
                        <td>{event.Venue}</td>
                        <td>
                          <div className="table-seats-cell">
                            <span><strong>{event.RemainingSeats}</strong> / {event.TotalSeats}</span>
                            <div className="table-seats-mini-bar">
                              <div
                                style={{
                                  width: `${event.TotalSeats ? Math.round(((event.TotalSeats - event.RemainingSeats) / event.TotalSeats) * 100) : 0}%`,
                                  background: event.RemainingSeats < 10 ? "#ef4444" : "#6366f1",
                                  height: "100%",
                                  borderRadius: "2px",
                                }}
                              />
                            </div>
                          </div>
                        </td>
                        <td className="table-price">₹{Number(event.ticketPrice || 0).toLocaleString("en-IN")}</td>
                        <td>
                          <div className="table-actions">
                            <button
                              className="btn btn-outline-sm"
                              onClick={() => navigate(`/admin/edit-event/${event.eventid}`)}
                              title="Edit Event"
                            >
                              ✏️ Edit
                            </button>
                            <button
                              className="btn btn-danger-sm"
                              onClick={() => handleDelete(event.eventid, event.Title)}
                              disabled={deletingId === event.eventid}
                              title="Delete Event"
                            >
                              {deletingId === event.eventid ? "..." : "🗑️ Delete"}
                            </button>
                          </div>
                        </td>
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

export default ManageEvents;
