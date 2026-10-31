
self.addEventListener('push', function(event) {
  const data = event.data ? event.data.json() : {};
  const title = data.title || 'رسالة جديدة من حبيبتك 💜';
  const options = {
    body: data.body || 'فتحي الموقع لتشوفي الرسالة',
    icon: 'https://cdn-icons-png.flaticon.com/512/833/833472.png',
    badge: 'https://cdn-icons-png.flaticon.com/512/833/833472.png',
    vibrate: [200, 100, 200],
    tag: 'sahmed-msg'
  };
  event.waitUntil(self.registration.showNotification(title, options));
});
self.addEventListener('notificationclick', function(event) {
  event.notification.close();
  event.waitUntil(clients.openWindow('/'));
});
