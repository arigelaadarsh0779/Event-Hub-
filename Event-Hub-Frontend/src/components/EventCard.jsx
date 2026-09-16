import { useNavigate } from "react-router-dom";

// Displays a single event in card format.
// Field names match exactly what the backend returns (Title, Date, Venue etc.)
const EventCard = ({ event }) => {
  const navigate = useNavigate();

  const formatDate = (dateStr) => {
    if (!dateStr) return "TBA";
    return new Date(dateStr).toLocaleDateString("en-IN", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
  };

  const formatTime = (timeStr) => {
    if (!timeStr) return "";
    return timeStr.slice(0, 5); // Show HH:MM
  };

  const getThemeColor = (theme) => {
    const colors = {
      music: "#8b5cf6",
      tech: "#3b82f6",
      sports: "#22c55e",
      food: "#f97316",
      art: "#ec4899",
      education: "#06b6d4",
      business: "#64748b",
    };
    const lowerTheme = (theme || "").toLowerCase();
    for (const key of Object.keys(colors)) {
      if (lowerTheme.includes(key)) return colors[key];
    }
    return "#6366f1";
  };

  const themeColor = getThemeColor(event.ThemeOfTheProject);
  const seatsPercent = event.TotalSeats
    ? Math.round(((event.TotalSeats - event.RemainingSeats) / event.TotalSeats) * 100)
    : 0;

  return (
    <div className="event-card" onClick={() => navigate(`/events/${event.eventid}`)}>
      {/* Theme badge */}
      <div className="event-card-header" style={{ background: `linear-gradient(135deg, ${themeColor}22, ${themeColor}44)` }}>
        <span className="event-theme-badge" style={{ background: themeColor }}>
          {event.ThemeOfTheProject || "General"}
        </span>
        <span className="event-price">₹{event.ticketPrice?.toLocaleString("en-IN") || "Free"}</span>
      </div>

      <div className="event-card-body">
        <h3 className="event-title">{event.Title}</h3>
        <p className="event-description">{event.Description}</p>

        <div className="event-meta">
          <div className="event-meta-item">
            <span className="meta-icon">📅</span>
            <span>{formatDate(event.Date)}</span>
          </div>
          <div className="event-meta-item">
            <span className="meta-icon">🕐</span>
            <span>{formatTime(event.StartTime)} – {formatTime(event.EndTime)}</span>
          </div>
          <div className="event-meta-item">
            <span className="meta-icon">📍</span>
            <span>{event.Venue}</span>
          </div>
          <div className="event-meta-item">
            <span className="meta-icon">👤</span>
            <span>{event.Organizer}</span>
          </div>
        </div>

        {/* Seats progress bar */}
        <div className="seats-info">
          <div className="seats-bar-container">
            <div
              className="seats-bar-fill"
              style={{
                width: `${seatsPercent}%`,
                background: event.RemainingSeats < 10 ? "#ef4444" : themeColor,
              }}
            />
          </div>
          <span className={`seats-count ${event.RemainingSeats < 10 ? "seats-low" : ""}`}>
            {event.RemainingSeats} seats left
          </span>
        </div>
      </div>

      <div className="event-card-footer">
        <button
          className="btn btn-primary w-full"
          onClick={(e) => {
            e.stopPropagation();
            navigate(`/events/${event.eventid}`);
          }}
        >
          View Event →
        </button>
      </div>
    </div>
  );
};

export default EventCard;
