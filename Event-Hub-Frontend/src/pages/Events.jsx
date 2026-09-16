import { useEffect, useState } from "react";
import { getAllEvents, findEventsByTheme, findEventsByVenue } from "../services/eventService";
import EventCard from "../components/EventCard";
import Loading from "../components/Loading";

const Events = () => {
  const [events, setEvents] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState("");
  const [filterTheme, setFilterTheme] = useState("All");

  useEffect(() => {
    getAllEvents()
      .then((res) => {
        setEvents(res.data);
        setFiltered(res.data);
      })
      .catch((err) => {
        setError(err.response?.data || "Failed to load events. Please try again.");
      })
      .finally(() => setLoading(false));
  }, []);

  // Client-side filtering for search and theme
  useEffect(() => {
    let result = [...events];
    if (search.trim()) {
      const q = search.toLowerCase();
      result = result.filter(
        (e) =>
          e.Title?.toLowerCase().includes(q) ||
          e.Venue?.toLowerCase().includes(q) ||
          e.Organizer?.toLowerCase().includes(q) ||
          e.Description?.toLowerCase().includes(q)
      );
    }
    if (filterTheme !== "All") {
      result = result.filter((e) =>
        e.ThemeOfTheProject?.toLowerCase().includes(filterTheme.toLowerCase())
      );
    }
    setFiltered(result);
  }, [search, filterTheme, events]);

  // Get unique themes for filter pills
  const themes = ["All", ...new Set(events.map((e) => e.ThemeOfTheProject).filter(Boolean))];

  if (loading) return <div className="page"><Loading message="Loading events..." /></div>;

  if (error) return (
    <div className="page">
      <div className="container">
        <div className="error-box">
          <p>⚠️ {error}</p>
          <button className="btn btn-primary" onClick={() => window.location.reload()}>Try Again</button>
        </div>
      </div>
    </div>
  );

  return (
    <div className="page">
      <div className="container">
        {/* Page Header */}
        <div className="page-header">
          <h1 className="page-title">All Events</h1>
          <p className="page-subtitle">{events.length} events available • Book your spot today</p>
        </div>

        {/* Search + Filter */}
        <div className="events-toolbar">
          <div className="search-wrapper">
            <span className="search-icon">🔍</span>
            <input
              type="text"
              className="search-input"
              placeholder="Search by title, venue, organizer..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              id="events-search"
            />
            {search && (
              <button className="search-clear" onClick={() => setSearch("")}>✕</button>
            )}
          </div>

          {/* Theme pills */}
          <div className="filter-pills">
            {themes.map((theme) => (
              <button
                key={theme}
                className={`filter-pill ${filterTheme === theme ? "filter-pill-active" : ""}`}
                onClick={() => setFilterTheme(theme)}
              >
                {theme}
              </button>
            ))}
          </div>
        </div>

        {/* Results */}
        {filtered.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">🎭</div>
            <h3>No events found</h3>
            <p>Try adjusting your search or filters</p>
            <button className="btn btn-outline" onClick={() => { setSearch(""); setFilterTheme("All"); }}>
              Clear Filters
            </button>
          </div>
        ) : (
          <>
            <p className="results-count">{filtered.length} event{filtered.length !== 1 ? "s" : ""} found</p>
            <div className="events-grid">
              {filtered.map((event) => (
                <EventCard key={event.eventid} event={event} />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Events;
