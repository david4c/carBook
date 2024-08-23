package com.example.carbook.model.dto;

import java.util.List;

public record PaginatedMaintenanceRecordResponse(
        List<MaintenanceRecordResponse> records,
        int currentPage,
        int totalPages
) {
}
