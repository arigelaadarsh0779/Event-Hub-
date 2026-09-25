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
    getAllEvents()
      .then((res) => setEvents(res.data.slice(0, 6)))
      .catch(() => setError("Could not load events"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <section className="hero">
        <div className="hero-bg">
          <div className="hero-orb hero-orb-1"></div>
          <div className="hero-orb hero-orb-2"></div>
          <div className="hero-orb hero-orb-3"></div>
        </div>

        <div className="container hero-container">
          <div className="hero-grid">
            <div className="hero-content">
              <div className="hero-badge">✨ Live Entertainment & Conference Hub</div>
              <h1 className="hero-title">
                Discover.<br />
                <span className="hero-title-accent">Book Tickets.</span><br />
                Experience Live.
              </h1>
              <p className="hero-subtitle">
                Explore tech summits, music festivals, coding hackathons, and cultural shows.
                Browse all events freely and reserve your spot instantly with secure Razorpay checkout!
              </p>
              <div className="hero-actions">
                <Link to="/events" className="btn btn-hero-primary">
                  🎟️ Explore All Events
                </Link>
                {isAuthenticated ? (
                  <Link to="/my-bookings" className="btn btn-hero-secondary">
                    📋 My Bookings
                  </Link>
                ) : (
                  <Link to="/register" className="btn btn-hero-secondary">
                    🚀 Create Free Account
                  </Link>
                )}
              </div>
              <div className="hero-stats">
                <div className="hero-stat">
                  <span className="stat-number">500+</span>
                  <span className="stat-label">Events Hosted</span>
                </div>
                <div className="hero-stat-divider"></div>
                <div className="hero-stat">
                  <span className="stat-number">10K+</span>
                  <span className="stat-label">Happy Attendees</span>
                </div>
                <div className="hero-stat-divider"></div>
                <div className="hero-stat">
                  <span className="stat-number">100%</span>
                  <span className="stat-label">Instant Tickets</span>
                </div>
              </div>
            </div>

            <div className="hero-graphic-card">
              <div className="hero-image-wrapper">
                <img
                  src="/images/hero_banner.jpg"
                  alt="Live Concert and Events"
                  className="hero-main-img"
                />
                <div className="hero-floating-badge top-right">
                  <span className="pulse-dot"></span> 🌟 Trending Live
                </div>
                <div className="hero-floating-badge bottom-left">
                  ⚡ Real-Time Seat Tracker
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Events Section */}
      <section className="section section-dark">
        <div className="container">
          <div className="section-header-flex">
            <div>
              <span className="badge-pill-cyan">🔥 Featured</span>
              <h2 className="section-title">Explore Trending Events</h2>
              <p className="section-subtitle">No login required to browse — find your next experience now</p>
            </div>
            <Link to="/events" className="btn btn-outline">
              View All ({events.length}+) →
            </Link>
          </div>

          {loading ? (
            <Loading message="Loading featured events..." />
          ) : error ? (
            <div className="error-box">⚠️ {error}</div>
          ) : events.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">🎪</div>
              <h3>No events available right now</h3>
              <p>Check back soon for new concerts, workshops, and meetups!</p>
            </div>
          ) : (
            <>
              <div className="events-grid">
                {events.map((event) => (
                  <EventCard key={event.eventid} event={event} />
                ))}
              </div>
              <div className="section-cta" style={{ marginTop: "3rem", textAlign: "center" }}>
                <Link to="/events" className="btn btn-primary btn-lg">
                  Browse All Events & Tickets 🎟️
                </Link>
              </div>
            </>
          )}
        </div>
      </section>

      {/* How it works banner with Graphic */}
      <section className="section">
        <div className="container">
          <div className="banner-graphic-card">
            <div className="banner-graphic-content">
              <span className="badge-pill-purple">💡 Seamless Experience</span>
              <h2>Instant E-Tickets & Live QR Verification</h2>
              <p>
                Once you select your seats and pay via Razorpay, a high-resolution PDF ticket with a unique QR code is generated instantly and delivered directly to your email inbox.
              </p>
              <div className="steps-row">
                <div className="step-mini"><span className="step-num">1</span> Pick Event</div>
                <div className="step-mini"><span className="step-num">2</span> Select Seats</div>
                <div className="step-mini"><span className="step-num">3</span> Pay Online</div>
                <div className="step-mini"><span className="step-num">4</span> E-Ticket Sent 📧</div>
              </div>
            </div>
            <div className="banner-graphic-img-container">
              <img src="/images/category_banner.jpg" alt="Digital Event Ticket QR Code" className="banner-img" />
            </div>
          </div>
        </div>
      </section>

      {/* Features Grid */}
      <section className="section section-dark">
        <div className="container">
          <div className="section-header" style={{ textAlign: "center", marginBottom: "3rem" }}>
            <h2 className="section-title">Why Event Hub?</h2>
            <p className="section-subtitle">Designed for seamless event discovery and instant ticket reservations</p>
          </div>
          <div className="features-grid">
            {[
              { icon: "⚡", title: "Public Event Access", desc: "Browse full event schedules, venues, and seat availability without having to register first." },
              { icon: "🔒", title: "Razorpay Payments", desc: "100% secure payment gateway supporting UPI, Credit/Debit cards, and NetBanking." },
              { icon: "📧", title: "Instant QR E-Tickets", desc: "Automated PDF generation with embedded QR codes sent straight to your email." },
              { icon: "💺", title: "Live Seat Count", desc: "Real-time seat count updates so you never miss out on fast-selling events." },
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

