import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    username: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
    role: "USER",
  });
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      toast.error("Passwords do not match");
      return;
    }
    if (form.password.length < 6) {
      toast.error("Password must be at least 6 characters");
      return;
    }
    setLoading(true);
    try {
      await register({
        username: form.username.trim(),
        email: form.email.trim(),
        phone: form.phone.trim(),
        password: form.password,
        role: form.role,
      });
      toast.success("Account created successfully! Please sign in. 🎉");
      navigate("/login");
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || "Registration failed. Please try again.";
      toast.error(String(msg));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-logo">🎟️</div>
          <h1 className="auth-title">Create Account</h1>
          <p className="auth-subtitle">Join Event Hub today</p>
        </div>

        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label" htmlFor="username">Username</label>
            <div className="input-wrapper">
              <span className="input-icon">👤</span>
              <input
                id="username"
                type="text"
                name="username"
                className="form-input"
                placeholder="Choose a username"
                value={form.username}
                onChange={handleChange}
                autoComplete="username"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="email">Email</label>
            <div className="input-wrapper">
              <span className="input-icon">📧</span>
              <input
                id="email"
                type="email"
                name="email"
                className="form-input"
                placeholder="your.email@example.com"
                value={form.email}
                onChange={handleChange}
                autoComplete="email"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="phone">Phone Number</label>
            <div className="input-wrapper">
              <span className="input-icon">📱</span>
              <input
                id="phone"
                type="tel"
                name="phone"
                className="form-input"
                placeholder="10-digit mobile number"
                value={form.phone}
                onChange={handleChange}
                autoComplete="tel"
                required
              />
            </div>
          </div>

          {/* Account Role Selector */}
          <div className="form-group">
            <label className="form-label">Account Role</label>
            <div className="role-selector-pills">
              <button
                type="button"
                className={`role-pill ${form.role === "USER" ? "role-pill-active" : ""}`}
                onClick={() => setForm((prev) => ({ ...prev, role: "USER" }))}
              >
                👤 Regular User (Book Events)
              </button>
              <button
                type="button"
                className={`role-pill ${form.role === "ADMIN" ? "role-pill-active" : ""}`}
                onClick={() => setForm((prev) => ({ ...prev, role: "ADMIN" }))}
              >
                🛡️ Administrator (Manage Events)
              </button>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="password">Password</label>
            <div className="input-wrapper">
              <span className="input-icon">🔒</span>
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                name="password"
                className="form-input"
                placeholder="At least 6 characters"
                value={form.password}
                onChange={handleChange}
                autoComplete="new-password"
                required
              />
              <button
                type="button"
                className="input-icon-right"
                onClick={() => setShowPassword(!showPassword)}
                aria-label="Toggle password visibility"
              >
                {showPassword ? "🙈" : "👁️"}
              </button>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="confirmPassword">Confirm Password</label>
            <div className="input-wrapper">
              <span className="input-icon">🔒</span>
              <input
                id="confirmPassword"
                type={showPassword ? "text" : "password"}
                name="confirmPassword"
                className="form-input"
                placeholder="Re-enter password"
                value={form.confirmPassword}
                onChange={handleChange}
                autoComplete="new-password"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-full"
            disabled={loading}
            id="register-submit-btn"
          >
            {loading ? (
              <><span className="btn-spinner"></span> Creating Account...</>
            ) : (
              "Create Account"
            )}
          </button>
        </form>

        <p className="auth-footer">
          Already have an account?{" "}
          <Link to="/login" className="auth-link">Sign in →</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;
