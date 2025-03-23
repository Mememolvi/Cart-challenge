import matplotlib.pyplot as plt
from datetime import datetime
from collections import Counter, defaultdict

# File path (adjust as needed)
AOF_FILE = "../cart_data.aof"

# Colors for each fruit
COLORS = {
    "APPLE": "red",
    "BANANA": "yellow",
    "MELON": "green",
    "LIME": "cyan"
}

# Day names for plotting
DAY_NAMES = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"]

# Function to read and parse the AOF file
def load_aof_data(file_path):
    purchases = []
    try:
        with open(file_path, "r") as f:
            for line in f:
                if line.strip():
                    item, timestamp_str = line.strip().split(" ", 1)
                    timestamp = datetime.strptime(timestamp_str, "%Y-%m-%d %H:%M:%S.%f")
                    purchases.append((item, timestamp))
        return purchases
    except FileNotFoundError:
        print(f"Error: {file_path} not found.")
        return []
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
        return []

# Function to plot purchases across 24-hour day with size proportional to count
def plot_purchases_by_hour(purchases):
    # Group purchases by item and hour
    hour_counts = {}
    for item in COLORS.keys():
        hour_counts[item] = Counter(ts.hour for it, ts in purchases if it == item)

    # Prepare plot
    plt.figure(figsize=(12, 6))
    
    # Plot each fruit
    for item, counts in hour_counts.items():
        if counts:  # Only plot if there are purchases
            hours = list(counts.keys())
            counts_list = list(counts.values())
            sizes = [count * 50 for count in counts_list]
            
            plt.scatter(hours, counts_list, s=sizes, c=COLORS[item], label=item, alpha=0.6)

    # Customize plot
    plt.title("Purchases Across 24-Hour Day")
    plt.xlabel("Hour of Day (0-23)")
    plt.ylabel("Total Count")
    plt.xticks(range(24))
    plt.legend(title="Fruit")
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.savefig("purchases_by_hour.png")
    plt.close()

# Function to plot purchases by day of the week
def plot_purchases_by_day(purchases):
    # Group purchases by day of week and item
    day_counts = defaultdict(lambda: defaultdict(int))
    for item, timestamp in purchases:
        day_index = timestamp.weekday()  # 0 = Monday, 6 = Sunday
        day_counts[day_index][item] += 1

    # Prepare data for stacked bar chart
    days = range(7)  # 0-6 for MON-SUN
    bottom = [0] * 7  # Bottom of each stack

    plt.figure(figsize=(12, 6))
    
    # Plot stacked bars for each fruit
    for item in COLORS.keys():
        counts = [day_counts[day].get(item, 0) for day in days]
        if any(counts):  # Only plot if there are purchases
            plt.bar(DAY_NAMES, counts, bottom=bottom, color=COLORS[item], label=item)
            bottom = [b + c for b, c in zip(bottom, counts)]

    # Customize plot
    plt.title("Purchases by Day of the Week")
    plt.xlabel("Day of Week")
    plt.ylabel("Total Count")
    plt.legend(title="Fruit")
    plt.savefig("purchases_by_day.png")
    plt.close()

# Main execution
if __name__ == "__main__":
    # Load data from AOF file
    purchases = load_aof_data(AOF_FILE)
    
    if not purchases:
        print("No data to plot.")
    else:
        # Generate plots
        plot_purchases_by_hour(purchases)
        plot_purchases_by_day(purchases)
        print("Plots generated: purchases_by_hour.png, purchases_by_day.png")