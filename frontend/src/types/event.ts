export interface Event {
  id: string;
  title: string;
  date: string;
  time: string;
  location: string;
  price: number;
  ticketsLeft: number;
  totalTickets: number;
  image: string;
  category: string;
  ticketType: string;
}

export interface BookingFormData {
  fullName: string;
  email: string;
  phone: string;
  quantity: number;
}
