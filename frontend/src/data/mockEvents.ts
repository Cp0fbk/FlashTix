import { Event } from '../types/event';

export const mockEvents: Event[] = [
  {
    id: '1',
    title: 'Summer Rock Festival 2025',
    date: 'July 15, 2025',
    time: '6:00 PM',
    location: 'Central Park Arena',
    price: 89.99,
    ticketsLeft: 12,
    totalTickets: 500,
    image: 'https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800&h=600&fit=crop',
    category: 'Music',
    ticketType: 'General Admission'
  },
  {
    id: '2',
    title: 'Tech Conference 2025',
    date: 'August 22, 2025',
    time: '9:00 AM',
    location: 'Convention Center',
    price: 149.99,
    ticketsLeft: 45,
    totalTickets: 300,
    image: 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&h=600&fit=crop',
    category: 'Technology',
    ticketType: 'VIP Pass'
  },
  {
    id: '3',
    title: 'Indie Music Night',
    date: 'June 8, 2025',
    time: '8:00 PM',
    location: 'Blue Moon Theatre',
    price: 39.99,
    ticketsLeft: 8,
    totalTickets: 150,
    image: 'https://images.unsplash.com/photo-1501281668745-f7f57925c3b4?w=800&h=600&fit=crop',
    category: 'Music',
    ticketType: 'Standard'
  },
  {
    id: '4',
    title: 'Startup Networking Mixer',
    date: 'September 5, 2025',
    time: '7:00 PM',
    location: 'Innovation Hub',
    price: 25.00,
    ticketsLeft: 67,
    totalTickets: 200,
    image: 'https://images.unsplash.com/photo-1511578314322-379afb476865?w=800&h=600&fit=crop',
    category: 'Networking',
    ticketType: 'Early Bird'
  }
];
