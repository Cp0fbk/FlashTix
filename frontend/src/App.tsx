import { useState } from 'react';
import { Navbar } from './components/Navbar';
import { Hero } from './components/Hero';
import { EventCard } from './components/EventCard';
import { BookingModal } from './components/BookingModal';
import { mockEvents } from './data/mockEvents';
import { Event } from './types/event';

function App() {
  const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleBookNow = (event: Event) => {
    setSelectedEvent(event);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setTimeout(() => setSelectedEvent(null), 300);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <Hero />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-2">Trending Events</h2>
          <p className="text-gray-600">Book your tickets before they sell out</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {mockEvents.map((event) => (
            <EventCard key={event.id} event={event} onBookNow={handleBookNow} />
          ))}
        </div>
      </div>

      <BookingModal
        event={selectedEvent}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
      />
    </div>
  );
}

export default App;
