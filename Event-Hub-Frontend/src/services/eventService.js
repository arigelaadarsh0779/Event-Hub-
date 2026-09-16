import api from "./api";

// GET /api/user/getallaevents - Returns list of all events (needs JWT)
export const getAllEvents = () => api.get("/api/user/getallaevents");

// GET /api/user/title/{title} - Find one event by its exact title
export const findEventByTitle = (title) =>
  api.get(`/api/user/title/${encodeURIComponent(title)}`);

// GET /api/user/theme/{theme} - Get events by theme
export const findEventsByTheme = (theme) =>
  api.get(`/api/user/theme/${encodeURIComponent(theme)}`);

// GET /api/user/venue/{venue} - Get events by venue
export const findEventsByVenue = (venue) =>
  api.get(`/api/user/venue/${encodeURIComponent(venue)}`);
