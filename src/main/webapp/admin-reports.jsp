<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Reports - Admin</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .action-bar {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background-color: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
        }

        .report-section {
            margin-bottom: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
        }

        .report-section h2 {
            color: #2c3e50;
            margin-bottom: 15px;
        }

        .date-range {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
        }

        .date-range input {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .chart-container {
            width: 100%;
            height: 300px;
            margin-top: 20px;
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .stat-card {
            background-color: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        .stat-card h3 {
            color: #7f8c8d;
            font-size: 14px;
            margin: 0 0 10px 0;
        }

        .stat-card .value {
            color: #2c3e50;
            font-size: 24px;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Reports Dashboard</h1>
        
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-primary">Back to Admin Menu</a>
            <div class="date-range">
                <input type="date" id="startDate" onchange="updateReports()">
                <input type="date" id="endDate" onchange="updateReports()">
            </div>
        </div>

        <div class="report-section">
            <h2>Sales Overview</h2>
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Total Sales</h3>
                    <div class="value">$${totalSales}</div>
                </div>
                <div class="stat-card">
                    <h3>Total Orders</h3>
                    <div class="value">${totalOrders}</div>
                </div>
                <div class="stat-card">
                    <h3>Average Order Value</h3>
                    <div class="value">$${averageOrderValue}</div>
                </div>
                <div class="stat-card">
                    <h3>Total Customers</h3>
                    <div class="value">${totalCustomers}</div>
                </div>
            </div>
            <div class="chart-container" id="salesChart">
                <!-- Sales chart will be rendered here -->
            </div>
        </div>

        <div class="report-section">
            <h2>Product Performance</h2>
            <div class="chart-container" id="productChart">
                <!-- Product performance chart will be rendered here -->
            </div>
        </div>

        <div class="report-section">
            <h2>Customer Analytics</h2>
            <div class="chart-container" id="customerChart">
                <!-- Customer analytics chart will be rendered here -->
            </div>
        </div>
    </div>

    <script>
        function updateReports() {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            
            if (startDate && endDate) {
                // TODO: Implement report data fetching and chart updates
                alert('Report update functionality to be implemented');
            }
        }

        // Initialize date inputs with current month
        window.onload = function() {
            const today = new Date();
            const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
            const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

            document.getElementById('startDate').value = firstDay.toISOString().split('T')[0];
            document.getElementById('endDate').value = lastDay.toISOString().split('T')[0];
            
            updateReports();
        };
    </script>
</body>
</html> 