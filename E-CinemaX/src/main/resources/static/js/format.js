// Shared date/time formatting - the REST API returns ISO dates/times
// (e.g. "2026-08-18", "13:15"), these turn them into the display format
// the original UI used (e.g. "18TH AUGUST 2026", "1:15PM").

function formatDate(isoDate) {
	const date = new Date(isoDate + 'T00:00:00');
	return date.toLocaleDateString('en-US', { day: 'numeric', month: 'long', year: 'numeric' }).toUpperCase();
}

function formatTime(isoTime) {
	const [hours, minutes] = isoTime.split(':').map(Number);
	const period = hours >= 12 ? 'PM' : 'AM';
	const hour12 = ((hours + 11) % 12) + 1;
	return `${hour12}:${String(minutes).padStart(2, '0')}${period}`;
}
