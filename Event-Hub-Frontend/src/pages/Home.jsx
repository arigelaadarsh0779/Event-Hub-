import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getAllEvents } from "../services/eventService";
import EventCard from "../components/EventCard";
import Loading from "../components/Loading";
import { useAuth } from "../context/AuthContext";

const Home = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isAuthenticated) {
      getAllEvents()
        .then((res) => setEvents(res.data.slice(0, 6)))
        .catch(() => setError("Could not load events"))
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [isAuthenticated]);

  return (
    <>
      <section className="hero">
        <div className="hero-bg">
          <div className="hero-orb hero-orb-1"></div>
          <div className="hero-orb hero-orb-2"></div>
          <div className="hero-orb hero-orb-3"></div>
        </div>
        <div className="hero-content">
          <div className="hero-badge">🎟️ India&apos;s Event Platform</div>
          <h1 className="hero-title">
            Discover.<br />
            <span className="hero-title-accent">Book.</span><br />
            Experience.
          </h1>
          <p className="hero-subtitle">
            Find amazing events near you - concerts, tech talks, workshops, and more.
            Book your spot in seconds with secure Razorpay payments.
          </p>
          <div className="hero-actions">
            <Link to="/events" className="btn btn-hero-primary">Explore Events</Link>
            {isAuthenticated ? (
              <Link to="/my-bookings" className="btn btn-hero-secondary">My Bookings</Link>
            ) : (
              <Link to="/register" className="btn btn-hero-secondary">Get Started Free</Link>
            )}
          </div>
          <div className="hero-stats">
            <div className="hero-stat"><span className="stat-number">500+</span><span className="stat-label">Events</span></div>
            <div className="hero-stat-divider"></div>
            <div className="hero-stat"><span className="stat-number">10K+</span><span className="stat-label">Users</span></div>
            <div className="hero-stat-divider"></div>
            <div className="hero-stat"><span className="stat-number">100%</span><span className="stat-label">Secure</span></div>
          </div>
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="section-header">
            <h2 className="section-title">How It Works</h2>
            <p className="section-subtitle">Book your event in 4 simple steps</p>
          </div>
          <div className="steps-grid">
            {[
              { icon: "🔍", step: "01", title: "Browse Events", desc: "Explore hundreds of events across categories." },
              { icon: "🎟️", step: "02", title: "Select Seats", desc: "Pick the number of seats and see real-time availability." },
              { icon: "💳", step: "03", title: "Pay Securely", desc: "Complete your booking with Razorpay - trusted by millions." },
              { icon: "📧", step: "04", title: "Get Your Ticket", desc: "PDF ticket with QR code sent directly to your email." },
            ].map((item) => (
              <div key={item.step} className="step-card">
                <div className="step-icon">{item.icon}</div>
                <div className="step-number">{item.step}</div>
                <h3 className="step-title">{item.title}</h3>
                <p className="step-desc">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="section section-dark">
        <div className="container">
          <div className="section-header">
            <h2 className="section-title">Featured Events</h2>
            <p className="section-subtitle">{isAuthenticated ? "Hand-picked events just for you" : "Log in to see live events"}</p>
          </div>
          {!isAuthenticated ? (
            <div className="cta-box">
              <p>Create a free account to browse and book events</p>
              <div className="cta-actions">
                <Link to="/register" className="btn btn-primary">Register Now</Link>
                <Link to="/login" className="btn btn-outline">Sign In</Link>
              </div>
            </div>
          ) : loading ? (
            <Loading message="Loading featured events..." />
          ) : error ? (
            <div className="error-box">{error}</div>
          ) : events.length === 0 ? (
            <div className="empty-state"><p>No events available yet. Check back soon!</p></div>
          ) : (
            <>
              <div className="events-grid">
                {events.map((event) => (<EventCard key={event.eventid} event={event} />))}
              </div>
              <div className="section-cta">
                <Link to="/events" className="btn btn-primary">View All Events</Link>
              </div>
            </>
          )}
        </div>
      </section>

      <section className="section">
        <div className="container">
          <div className="features-grid">
            {[
              { icon: "🔒", title: "Secure Payments", desc: "Powered by Razorpay with HMAC signature verification" },
              { icon: "📧", title: "Instant Tickets", desc: "PDF ticket with QR code sent directly to your email" },
              { icon: "💺", title: "Real-time Seats", desc: "Live seat availability updated as people book" },
              { icon: "❌", title: "Easy Cancellation", desc: "Cancel pending bookings with instant seat restoration" },
            ].map((f) => (
              <div key={f.title} className="feature-card">
                <div className="feature-icon">{f.icon}</div>
                <h3 className="feature-title">{f.title}</h3>
                <p className="feature-desc">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
};

export default Home;
