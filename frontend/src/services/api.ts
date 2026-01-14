import { ApiResponse, PageResponse, PaginationParams } from '../types/api';
import { Event } from '../types/event';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class ApiService {
    private getAuthToken(): string | null {
        return localStorage.getItem('token');
    }

    private async request<T>(
        endpoint: string,
        options: RequestInit = {}
    ): Promise<T> {
        const token = this.getAuthToken();

        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...(options.headers as Record<string, string>),
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers,
        });

        if (!response.ok) {
            throw new Error(`API Error: ${response.statusText}`);
        }

        return response.json();
    }

    async getTickets(params: PaginationParams = {}): Promise<ApiResponse<PageResponse<Event>>> {
        const {
            page = 0,
            size = 10,
            sortDir = 'ASC',
            sortBy = 'title'
        } = params;

        const queryParams = new URLSearchParams({
            page: page.toString(),
            size: size.toString(),
            sortDir,
            sortBy,
        });

        return this.request<ApiResponse<PageResponse<Event>>>(
            `/api/users/events/tickets?${queryParams.toString()}`
        );
    }
}

export const apiService = new ApiService();
