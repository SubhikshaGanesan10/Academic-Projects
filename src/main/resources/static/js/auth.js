// Shared helpers for checking login state and logging out - used by any
// page whose nav needs to know whether someone is currently logged in.

// Returns the logged-in user's info ({id, firstName, lastName, email, role}),
// or null if nobody is logged in.
async function getCurrentUser() {
	const response = await fetch('/api/auth/me');
	if (!response.ok) {
		return null;
	}
	return response.json();
}

async function logout() {
	await fetch('/api/auth/logout', { method: 'POST' });
	window.location.href = 'Cinema.html';
}
