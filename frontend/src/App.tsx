import { useState, useEffect } from 'react';
import { Navbar } from '@/components/Navbar';
import { Hero } from '@/components/Hero';
import { EventCard } from '@/components/EventCard';
import { BookingModal } from '@/components/BookingModal';
import { Pagination } from '@/components/Pagination';
import { Event } from '@/types/event';
import { apiService } from '@/services/api';
import { PageResponse } from '@/types/api';

function App() {
  const [selectedEvent, setSelectedEvent] = useState<Event | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0); // Backend uses 0-based pagination
  const [pageSize] = useState(4);
  const [events, setEvents] = useState<Event[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch tickets from API
  useEffect(() => {
    const fetchTickets = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await apiService.getTickets({
          page: currentPage,
          size: pageSize,
          sortDir: 'ASC',
          sortBy: 'title'
        });

        const pageData: PageResponse<Event> = response.data;
        setEvents(pageData.content);
        setTotalPages(pageData.totalPages);
        setTotalElements(pageData.totalElements);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch tickets');
        console.error('Error fetching tickets:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchTickets();
  }, [currentPage, pageSize]);

  const handleBookNow = (event: Event) => {
    setSelectedEvent(event);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setTimeout(() => setSelectedEvent(null), 300);
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page - 1); // Convert to 0-based for backend
    // Scroll to top of events section smoothly
    window.scrollTo({ top: 400, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <Hero />

      <div className="max-w-7xl mx-auto px-3 sm:px-4 lg:px-8 py-6 sm:py-12">
        <div className="mb-5 sm:mb-8">
          <h2 className="text-2xl sm:text-3xl font-bold text-gray-900 mb-1 sm:mb-2">Trending Events</h2>
          <p className="text-sm sm:text-base text-gray-600">
            Book your tickets before they sell out
            {totalElements > 0 && (
              <span className="ml-2 text-indigo-600 font-semibold">
                ({totalElements} events available)
              </span>
            )}
          </p>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
          </div>
        )}

        {/* Error State */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <p className="text-red-800 text-sm sm:text-base">
              <strong>Error:</strong> {error}
            </p>
          </div>
        )}

        {/* Events Grid */}
        {!loading && !error && events.length > 0 && (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-3 sm:gap-6">
              {events.map((event) => (
                <EventCard key={event.id} event={event} onBookNow={handleBookNow} />
              ))}
            </div>

            <Pagination
              currentPage={currentPage + 1} // Convert to 1-based for display
              totalPages={totalPages}
              onPageChange={handlePageChange}
            />
          </>
        )}

        {/* Empty State */}
        {!loading && !error && events.length === 0 && (
          <div className="text-center py-20">
            <p className="text-gray-500 text-lg">No events available at the moment.</p>
          </div>
        )}
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
