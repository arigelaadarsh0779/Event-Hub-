import api from "./api";

// POST /api/user/payment/createorder
// Request: { bookingId: number }
// Response: { orderId, amount, currency, keyId, Status }
export const createOrder = (bookingId) =>
  api.post("/api/user/payment/createorder", { bookingId });

// POST /api/user/payment/verify
// Request: { razorpayPaymentId, razorpayOrderId, razorpaySignature }
// Response: "Payment verified successfully" (string) or 400 error
// IMPORTANT: Backend does the signature verification using Razorpay Utils
// Never do signature verification in the frontend!
export const verifyPayment = (dto) =>
  api.post("/api/user/payment/verify", dto);
