import api from "./api";

// POST /api/admin/addevent
// Body: RequestEventDto (Title, Description, Date, StartTime, EndTime, Venue, ThemeOfTheProject, Organizer, RemainingSeats, TotalSeats, ticketPrice)
export const addEvent = (dto) => api.post("/api/admin/addevent", dto);

// POST /api/admin/editevent/{eventid}
export const editEvent = (eventId, dto) => api.post(`/api/admin/editevent/${eventId}`, dto);

// DELETE /api/admin/delete/{eventid}
export const deleteEvent = (eventId) => api.delete(`/api/admin/delete/${eventId}`);

// GET /api/admin/allbookings
export const getAllBookings = () => api.get("/api/admin/allbookings");
