// config.js

// Leave this empty for Production on Render
// This allows the frontend to use the same domain (qrpu.onrender.com) automatically
const BASE_BACKEND_URL = ""; 

// Core API Groups
const USER_API        = `${BASE_BACKEND_URL}/api/users`;
const ADMIN_API       = `${BASE_BACKEND_URL}/api/admin`;
const BROADCAST_API   = `${BASE_BACKEND_URL}/api/broadcast`;
const CONTACT_API     = `${BASE_BACKEND_URL}/api/support`;
const TASK_API        = `${BASE_BACKEND_URL}/api/tasks`;
const REPORT_API      = `${BASE_BACKEND_URL}/api/reports`;
const AI_REPORT_API   = `${BASE_BACKEND_URL}/api/reports/generate`;

// Notifications and File Uploads
const NOTIFICATION_API = `${BASE_BACKEND_URL}/api/notifications`;
const FILE_UPLOAD_API = `${TASK_API}/upload`;
