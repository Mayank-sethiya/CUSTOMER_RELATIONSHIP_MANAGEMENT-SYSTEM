// config.js

// 👇 Change this one line when switching from local to your EC2 server (or Elastic IP)
const BASE_BACKEND_URL = "http://localhost:8080"; // e.g., "http://13.234.56.78:8080"

// Core API Groups
const USER_API        = `${BASE_BACKEND_URL}/api/users`;        // UserController
const ADMIN_API       = `${BASE_BACKEND_URL}/api/admin`;        // AdminController
const BROADCAST_API   = `${BASE_BACKEND_URL}/api/broadcast`;    // BroadcastController
const CONTACT_API     = `${BASE_BACKEND_URL}/api/support`;      // ContactController
const TASK_API        = `${BASE_BACKEND_URL}/api/tasks`;        // TaskController
const REPORT_API      = `${BASE_BACKEND_URL}/api/reports`;      // TaskReportController
const AI_REPORT_API   = `${BASE_BACKEND_URL}/api/reports/generate`; // AiReportController

// 🔔 New Notifications API (per-user read/clear tracking)
const NOTIFICATION_API = `${BASE_BACKEND_URL}/api/notifications`; // UserNotificationController

// 🔁 File Upload API (used in tasks)
const FILE_UPLOAD_API = `${TASK_API}/upload`;  // ✅ YES, add this line

// (Optional) Export for JS modules if needed
// export {
//   USER_API, ADMIN_API, BROADCAST_API, CONTACT_API,
//   TASK_API, REPORT_API, FILE_UPLOAD_API, AI_REPORT_API,
//   NOTIFICATION_API
// };
