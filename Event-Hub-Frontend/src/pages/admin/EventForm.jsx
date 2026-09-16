import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getAllEvents } from "../../services/eventService";
import { addEvent, editEvent } from "../../services/adminService";
import Loading from "../../components/Loading";
import toast from "react-hot-toast";

const EventForm = () => {
  const { eventId } = useParams();
  const navigate = useNavigate();
  const isEditing = !!eventId;

  const [loading, setLoading] = useState(isEditing);
  const [submitting, setSubmitting] = useState(false);

  // Form State matching RequestEventDto
  const [formData, setFormData] = useState({
    Title: "",
    Description: "",
    Date: "",
    StartTime: "10:00",
    EndTime: "18:00",
    Venue: "",
    ThemeOfTheProject: "Music",
    Organizer: "",
    TotalSeats: 100,
    RemainingSeats: 100,
    ticketPrice: 499,
  });

  useEffect(() => {
    if (isEditing) {
      getAllEvents()
        .then((res) => {
          const found = res.data.find((e) => String(e.eventid) === String(eventId));
          if (!found) {
            toast.error("Event not found");
            navigate("/admin/events");
            return;
          }
          setFormData({
            Title: found.Title || "",
            Description: found.Description || "",
            Date: found.Date || "",
            StartTime: found.StartTime ? found.StartTime.slice(0, 5) : "10:00",
            EndTime: found.EndTime ? found.EndTime.slice(0, 5) : "18:00",
            Venue: found.Venue || "",
            ThemeOfTheProject: found.ThemeOfTheProject || "General",
            Organizer: found.Organizer || "",
            TotalSeats: found.TotalSeats || 100,
            RemainingSeats: found.RemainingSeats !== undefined ? found.RemainingSeats : 100,
            ticketPrice: found.ticketPrice || 0,
          });
        })
        .catch(() => {
          toast.error("Failed to load event data");
          navigate("/admin/events");
        })
        .finally(() => setLoading(false));
    }
  }, [eventId, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => {
      const updated = { ...prev, [name]: value };
      // When adding new event, remaining seats automatically match total seats
      if (!isEditing && name === "TotalSeats") {
        updated.RemainingSeats = Number(value);
      }
      return updated;
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Frontend validation
    if (!formData.Title.trim()) return toast.error("Title is required");
    if (!formData.Venue.trim()) return toast.error("Venue is required");
    if (!formData.Date) return toast.error("Date is required");
    if (!formData.Organizer.trim()) return toast.error("Organizer is required");

    // Format times to HH:MM:SS for Spring Boot LocalTime
    const formatTime = (t) => (t && t.length === 5 ? `${t}:00` : t || "10:00:00");

    const payload = {
      Title: formData.Title.trim(),
      Description: formData.Description.trim(),
      Date: formData.Date,
      StartTime: formatTime(formData.StartTime),
      EndTime: formatTime(formData.EndTime),
      Venue: formData.Venue.trim(),
      ThemeOfTheProject: formData.ThemeOfTheProject.trim(),
      Organizer: formData.Organizer.trim(),
      TotalSeats: Number(formData.TotalSeats),
      RemainingSeats: Number(formData.RemainingSeats),
      ticketPrice: Number(formData.ticketPrice),
    };

    setSubmitting(true);
    try {
      if (isEditing) {
        await editEvent(Number(eventId), payload);
        toast.success("Event updated successfully! ✨");
      } else {
        await addEvent(payload);
        toast.success("Event added successfully! 🎉");
      }
      navigate("/admin/events");
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || "Operation failed. Check backend logs.";
      toast.error(String(msg));
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="page"><Loading message="Loading event details..." /></div>;

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: "800px" }}>
        <button className="back-btn" onClick={() => navigate("/admin/events")}>
          ← Back to Manage Events
        </button>

        <div className="admin-form-card">
          <div className="admin-form-header">
            <span className="admin-badge-pill">🛡️ Admin Portal</span>
            <h1 className="page-title">{isEditing ? "Edit Event" : "Create New Event"}</h1>
            <p className="page-subtitle">
              {isEditing ? "Update details for this event" : "Fill in the details below to publish a new event"}
            </p>
          </div>

          <form className="admin-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label" htmlFor="Title">Event Title *</label>
              <input
                id="Title"
                type="text"
                name="Title"
                className="form-input"
                placeholder="e.g. Annual Tech Symposium 2026"
                value={formData.Title}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-grid-2">
              <div className="form-group">
                <label className="form-label" htmlFor="ThemeOfTheProject">Category / Theme *</label>
                <select
                  id="ThemeOfTheProject"
                  name="ThemeOfTheProject"
                  className="form-select"
                  value={formData.ThemeOfTheProject}
                  onChange={handleChange}
                  required
                >
                  <option value="Music">Music & Concerts</option>
                  <option value="Tech">Technology & Coding</option>
                  <option value="Workshop">Workshop & Education</option>
                  <option value="Sports">Sports & Fitness</option>
                  <option value="Food">Food & Drink</option>
                  <option value="Art">Art & Culture</option>
                  <option value="Business">Business & Networking</option>
                  <option value="General">General</option>
                </select>
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="Organizer">Organizer Name *</label>
                <input
                  id="Organizer"
                  type="text"
                  name="Organizer"
                  className="form-input"
                  placeholder="e.g. Google Developer Group"
                  value={formData.Organizer}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="Venue">Venue / Location *</label>
              <input
                id="Venue"
                type="text"
                name="Venue"
                className="form-input"
                placeholder="e.g. HITEX Exhibition Centre, Hyderabad"
                value={formData.Venue}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-grid-3">
              <div className="form-group">
                <label className="form-label" htmlFor="Date">Date *</label>
                <input
                  id="Date"
                  type="date"
                  name="Date"
                  className="form-input"
                  value={formData.Date}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="StartTime">Start Time *</label>
                <input
                  id="StartTime"
                  type="time"
                  name="StartTime"
                  className="form-input"
                  value={formData.StartTime}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="EndTime">End Time *</label>
                <input
                  id="EndTime"
                  type="time"
                  name="EndTime"
                  className="form-input"
                  value={formData.EndTime}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-grid-3">
              <div className="form-group">
                <label className="form-label" htmlFor="ticketPrice">Ticket Price (₹) *</label>
                <input
                  id="ticketPrice"
                  type="number"
                  name="ticketPrice"
                  min="0"
                  step="1"
                  className="form-input"
                  value={formData.ticketPrice}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="TotalSeats">Total Seats *</label>
                <input
                  id="TotalSeats"
                  type="number"
                  name="TotalSeats"
                  min="1"
                  className="form-input"
                  value={formData.TotalSeats}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="RemainingSeats">
                  {isEditing ? "Remaining Seats *" : "Initial Available Seats"}
                </label>
                <input
                  id="RemainingSeats"
                  type="number"
                  name="RemainingSeats"
                  min="0"
                  max={formData.TotalSeats}
                  className="form-input"
                  value={formData.RemainingSeats}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="Description">Event Description *</label>
              <textarea
                id="Description"
                name="Description"
                rows="4"
                className="form-input"
                style={{ resize: "vertical", minHeight: "100px" }}
                placeholder="Describe what attendees can expect, schedule, speakers, etc."
                value={formData.Description}
                onChange={handleChange}
                required
              />
            </div>

            <div className="admin-form-actions">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={submitting}
                style={{ minWidth: "180px" }}
              >
                {submitting ? (
                  <><span className="btn-spinner"></span> Saving...</>
                ) : isEditing ? (
                  "Save Changes ✨"
                ) : (
                  "Publish Event 🚀"
                )}
              </button>
              <button
                type="button"
                className="btn btn-outline"
                onClick={() => navigate("/admin/events")}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EventForm;
