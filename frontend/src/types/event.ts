export interface Ticket {
  name: string;
  price: number;
  remainingQuantity: number;
  initialQuantity: number;
}

export interface Event {
  id: number;
  title: string;
  startTime: string;
  location: string;
  bannerUrl: string;
  tickets: Ticket[];
}

export interface BookingFormData {
  fullName: string;
  email: string;
  phone: string;
  quantity: number;
  ticketType: string;
}
