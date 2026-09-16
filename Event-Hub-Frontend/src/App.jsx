import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Events from "./pages/Events";
import EventDetails from "./pages/EventDetails";
import Payment from "./pages/Payment";
import PaymentSuccess from "./pages/PaymentSuccess";
import MyBookings from "./pages/MyBookings";
import MyTickets from "./pages/MyTickets";

// Admin Pages
import ManageEvents from "./pages/admin/ManageEvents";
import EventForm from "./pages/admin/EventForm";
import AllBookings from "./pages/admin/AllBookings";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: "#111827",
              color: "#fff",
              border: "1px solid rgba(255, 255, 255, 0.15)",
              borderRadius: "12px",
              padding: "12px 16px",
            },
            success: {
              iconTheme: { primary: "#10b981", secondary: "#fff" },
            },
            error: {
              iconTheme: { primary: "#ef4444", secondary: "#fff" },
            },
          }}
        />

        <Navbar />

        <main className="main-content">
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Shared routes - accessible to both logged-in User and Admin */}
            <Route path="/events" element={
              <ProtectedRoute><Events /></ProtectedRoute>
            } />
            <Route path="/events/:eventId" element={
              <ProtectedRoute><EventDetails /></ProtectedRoute>
            } />

            {/* User-only booking & ticket routes */}
            <Route path="/payment/:bookingId" element={
              <ProtectedRoute><Payment /></ProtectedRoute>
            } />
            <Route path="/payment-success" element={
              <ProtectedRoute><PaymentSuccess /></ProtectedRoute>
            } />
            <Route path="/my-bookings" element={
              <ProtectedRoute><MyBookings /></ProtectedRoute>
            } />
            <Route path="/my-tickets" element={
              <ProtectedRoute><MyTickets /></ProtectedRoute>
            } />

            {/* Admin-only routes */}
            <Route path="/admin/events" element={
              <AdminRoute><ManageEvents /></AdminRoute>
            } />
            <Route path="/admin/add-event" element={
              <AdminRoute><EventForm /></AdminRoute>
            } />
            <Route path="/admin/edit-event/:eventId" element={
              <AdminRoute><EventForm /></AdminRoute>
            } />
            <Route path="/admin/bookings" element={
              <AdminRoute><AllBookings /></AdminRoute>
            } />

            {/* Catch-all 404 */}
            <Route path="*" element={
              <div className="page">
                <div className="container" style={{ textAlign: "center", paddingTop: "4rem" }}>
                  <h1 style={{ fontSize: "4rem", marginBottom: "1rem" }}>404</h1>
                  <p style={{ color: "#94a3b8" }}>The page you are looking for does not exist.</p>
                  <a href="/" className="btn btn-primary" style={{ marginTop: "1.5rem", display: "inline-block" }}>
                    Back to Home
                  </a>
                </div>
              </div>
            } />
          </Routes>
        </main>

        <footer className="footer">
          <div className="container">
            <p>© Event Hub — Discover, Book & Manage Live Events</p>
          </div>
        </footer>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
