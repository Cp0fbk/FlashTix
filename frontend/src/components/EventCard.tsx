import { Calendar, MapPin, Ticket, AlertCircle, Tag } from 'lucide-react';
import { Event } from '@/types/event';

interface EventCardProps {
  event: Event;
  onBookNow: (event: Event) => void;
}

export function EventCard({ event, onBookNow }: EventCardProps) {
  const minPrice = event.tickets?.length
    ? Math.min(...event.tickets.map(t => t.price))
    : 0;

  const ticketsLeft = event.tickets?.reduce((acc, t) => acc + t.remainingQuantity, 0) || 0;
  const totalTickets = event.tickets?.reduce((acc, t) => acc + (t.initialQuantity || 0), 0) || 0;
  const ticketPercentage = totalTickets > 0 ? (ticketsLeft / totalTickets) * 100 : 0;
  const isLowStock = ticketPercentage < 15;

  const eventDate = new Date(event.startTime);
  const dateStr = eventDate.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  });
  const timeStr = eventDate.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit'
  });

  const ticketTypes = event.tickets?.map(t => t.name).join(', ') || 'Standard';

  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-xl flex flex-col justify-between transition-shadow duration-300 overflow-hidden group">
      <div className="relative overflow-hidden h-36 sm:h-48">
        <img
          src={event.bannerUrl}
          alt={event.title}
          className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
        />
        <div className="absolute top-2 sm:top-3 right-2 sm:right-3 bg-white px-2 sm:px-3 py-0.5 sm:py-1 rounded-full text-xs sm:text-sm font-bold text-indigo-600">
          ${minPrice}
        </div>
        {isLowStock && (
          <div className="absolute top-2 sm:top-3 left-2 sm:left-3 bg-red-500 text-white px-2 sm:px-3 py-0.5 sm:py-1 rounded-full text-xs font-semibold flex items-center space-x-1">
            <AlertCircle className="w-3 h-3" />
            <span>Low Stock</span>
          </div>
        )}
      </div>

      <div className="px-3 sm:px-5 pt-3 sm:pt-5 flex-grow">
        <div className="mb-2 sm:mb-3">
          <h3 className="text-base sm:text-xl font-bold text-gray-900 mt-1 mb-2 sm:mb-3 line-clamp-2">
            {event.title}
          </h3>
        </div>

        <div className="space-y-1.5 sm:space-y-2 mb-3 sm:mb-4">
          <div className="flex items-center text-xs sm:text-sm text-gray-600">
            <Calendar className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2 text-gray-400 flex-shrink-0" />
            <span className="truncate">{dateStr} at {timeStr}</span>
          </div>
          <div className="flex items-center text-xs sm:text-sm text-gray-600">
            <MapPin className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2 text-gray-400 flex-shrink-0" />
            <span className="truncate">{event.location}</span>
          </div>
          <div className="flex items-center text-xs sm:text-sm text-gray-600">
            <Tag className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1.5 sm:mr-2 text-gray-400 flex-shrink-0" />
            <span className="truncate">{ticketTypes}</span>
          </div>
        </div>

        <div className="mb-3 sm:mb-4">
          <div className="flex items-center justify-between text-xs sm:text-sm mb-1.5 sm:mb-2">
            <span className="text-gray-600 flex items-center">
              <Ticket className="w-3.5 h-3.5 sm:w-4 sm:h-4 mr-1" />
              Tickets Left
            </span>
            <span className={`font-bold text-xs sm:text-sm ${isLowStock ? 'text-red-600' : 'text-gray-900'}`}>
              {ticketsLeft} available
            </span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-1.5 sm:h-2 overflow-hidden">
            <div
              className={`h-full rounded-full transition-all duration-300 ${isLowStock ? 'bg-red-500' : 'bg-indigo-600'
                }`}
              style={{ width: `${Math.max(ticketPercentage, 5)}%` }}
            ></div>
          </div>
        </div>

      </div>
      <div className="px-3 sm:px-5 pb-3 sm:pb-5">
        <button
          onClick={() => onBookNow(event)}
          className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 sm:py-3 px-3 sm:px-4 rounded-lg transition-colors duration-200 flex items-center justify-center space-x-1.5 sm:space-x-2 text-sm sm:text-base"
        >
          <Ticket className="w-4 h-4 sm:w-5 sm:h-5" />
          <span>Book Now</span>
        </button>
      </div>
    </div>
  );
}
