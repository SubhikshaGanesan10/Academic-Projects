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
	// Absolute path (leading /) so this works correctly no matter which
	// folder depth the calling page lives at (e.g. tickets/checkout.html).
	window.location.href = '/Cinema.html';
}
