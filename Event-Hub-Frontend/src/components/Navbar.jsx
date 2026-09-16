import { Link, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useState } from "react";

const Navbar = () => {
  const { isAuthenticated, isAdmin, user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login");
    setMenuOpen(false);
  };

  const isActive = (path) => location.pathname === path ? "nav-link active" : "nav-link";

  return (
    <nav className="navbar">
      <div className="navbar-container">
        {/* Logo */}
        <Link to="/" className="navbar-logo" onClick={() => setMenuOpen(false)}>
          <span className="logo-icon">🎟️</span>
          <span className="logo-text">Event<span className="logo-accent">Hub</span></span>
          {isAdmin && <span className="admin-tag">ADMIN</span>}
        </Link>

        {/* Hamburger - mobile */}
        <button
          className={`hamburger ${menuOpen ? "open" : ""}`}
          onClick={() => setMenuOpen(!menuOpen)}
          aria-label="Toggle menu"
        >
          <span></span><span></span><span></span>
        </button>

        {/* Nav Links */}
        <div className={`nav-links ${menuOpen ? "nav-open" : ""}`}>
          <Link to="/" className={isActive("/")} onClick={() => setMenuOpen(false)}>Home</Link>
          <Link to="/events" className={isActive("/events")} onClick={() => setMenuOpen(false)}>Events</Link>

          {/* ADMIN ONLY LINKS */}
          {isAuthenticated && isAdmin && (
            <>
              <Link to="/admin/events" className={isActive("/admin/events")} onClick={() => setMenuOpen(false)}>
                ⚙️ Manage Events
              </Link>
              <Link to="/admin/add-event" className={isActive("/admin/add-event")} onClick={() => setMenuOpen(false)}>
                ➕ Add Event
              </Link>
              <Link to="/admin/bookings" className={isActive("/admin/bookings")} onClick={() => setMenuOpen(false)}>
                📋 All Bookings
              </Link>
            </>
          )}

          {/* REGULAR USER ONLY LINKS (Admin does not book tickets) */}
          {isAuthenticated && !isAdmin && (
            <>
              <Link to="/my-bookings" className={isActive("/my-bookings")} onClick={() => setMenuOpen(false)}>
                My Bookings
              </Link>
              <Link to="/my-tickets" className={isActive("/my-tickets")} onClick={() => setMenuOpen(false)}>
                My Tickets
              </Link>
            </>
          )}

          <div className="nav-auth">
            {isAuthenticated ? (
              <>
                <span className={`nav-username ${isAdmin ? "admin-badge-pill" : ""}`}>
                  {isAdmin ? "🛡️ " : "👤 "} {user?.username}
                </span>
                <button className="btn btn-outline-sm" onClick={handleLogout}>
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-outline-sm" onClick={() => setMenuOpen(false)}>
                  Login
                </Link>
                <Link to="/register" className="btn btn-primary-sm" onClick={() => setMenuOpen(false)}>
                  Register
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
