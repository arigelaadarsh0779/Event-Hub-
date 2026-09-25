import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getUserProfile, updateUserProfile } from "../services/authService";
import { getErrorMessage } from "../services/api";
import Loading from "../components/Loading";
import toast from "react-hot-toast";

const Profile = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [form, setForm] = useState({
    username: "",
    email: "",
    phone: "",
    password: "",
    role: "USER",
  });
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    if (user?.userId) {
      getUserProfile(user.userId)
        .then((res) => {
          setForm({
            username: res.data.username || "",
            email: res.data.email || "",
            phone: res.data.phone || "",
            password: "",
            role: res.data.role || "USER",
          });
        })
        .catch((err) => {
          toast.error(getErrorMessage(err, "Failed to load profile details"));
        })
        .finally(() => setLoading(false));
    }
  }, [user]);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setUpdating(true);
    try {
      const payload = {
        username: form.username.trim(),
        email: form.email.trim(),
        phone: form.phone.trim(),
      };

      if (form.password && form.password.trim()) {
        payload.password = form.password.trim();
      }

      const res = await updateUserProfile(user.userId, payload);

      // Sync local storage username if username was updated
      if (res.data.username) {
        localStorage.setItem("username", res.data.username);
      }

      toast.success("Profile updated successfully! 🎉");
      setForm((prev) => ({ ...prev, password: "" }));
    } catch (err) {
      toast.error(getErrorMessage(err, "Failed to update profile"));
    } finally {
      setUpdating(false);
    }
  };

  if (loading) {
    return <div className="page"><Loading message="Loading profile..." /></div>;
  }

  return (
    <div className="page">
      <div className="container" style={{ maxWidth: "600px" }}>
        <div className="page-header" style={{ textAlign: "center", marginBottom: "2rem" }}>
          <div className="auth-logo">👤</div>
          <h1 className="page-title">My Account Profile</h1>
          <p className="page-subtitle">Manage your personal details & contact information</p>
        </div>

        <div className="auth-card" style={{ maxWidth: "100%", margin: "0 auto" }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1.5rem", paddingBottom: "1rem", borderBottom: "1px solid var(--border-subtle)" }}>
            <div>
              <span className="info-label">Account Type</span>
              <div style={{ marginTop: "0.25rem" }}>
                <span className={`badge ${form.role === "ADMIN" ? "badge-pill-purple" : "badge-pill-cyan"}`}>
                  {form.role === "ADMIN" ? "🛡️ Administrator" : "👤 Regular User"}
                </span>
              </div>
            </div>
            <div>
              <span className="info-label">User ID</span>
              <div className="info-value" style={{ fontSize: "0.95rem" }}>#{user?.userId}</div>
            </div>
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
                  value={form.username}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="email">Email Address</label>
              <div className="input-wrapper">
                <span className="input-icon">📧</span>
                <input
                  id="email"
                  type="email"
                  name="email"
                  className="form-input"
                  value={form.email}
                  onChange={handleChange}
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
                  value={form.phone}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="password">
                New Password <span style={{ color: "var(--text-muted)", fontWeight: "normal" }}>(leave blank to keep current)</span>
              </label>
              <div className="input-wrapper">
                <span className="input-icon">🔒</span>
                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  name="password"
                  className="form-input"
                  placeholder="Enter new password"
                  value={form.password}
                  onChange={handleChange}
                />
                <button
                  type="button"
                  className="input-icon-right"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? "🙈" : "👁️"}
                </button>
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary btn-full"
              disabled={updating}
              id="profile-save-btn"
              style={{ marginTop: "1rem" }}
            >
              {updating ? (
                <><span className="btn-spinner"></span> Saving Changes...</>
              ) : (
                "Save Profile Changes 💾"
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Profile;
