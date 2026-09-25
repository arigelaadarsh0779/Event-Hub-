import api from "./api";

// POST /api/user/createbooking
// Backend expects: { eventId, userId, numberOfSeats }
export const createBooking = (dto) =>
  api.post("/api/user/createbooking", dto);

// GET /api/user/{userId} - Get all bookings for a specific user
// NOTE: Despite the endpoint name using "bookingId", it actually takes userId
export const getBookingsByUserId = (userId) =>
  api.get(`/api/user/${userId}`);

// PUT /api/user/cancel/{bookingId} - Cancel a booking
export const cancelBooking = (bookingId) =>
  api.put(`/api/user/cancel/${bookingId}`);

// GET /api/user/availability/{eventId} - Get remaining seats for an event
export const checkAvailability = (eventId) =>
  api.get(`/api/user/availability/${eventId}`);

// PUT /api/user/confirm/{bookingId} - Confirm booking & trigger booking success email
export const confirmBooking = (bookingId) =>
  api.put(`/api/user/confirm/${bookingId}`);
