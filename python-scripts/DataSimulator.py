import random
from datetime import datetime, timedelta
import argparse

AOF_FILE = "../cart_data.aof"

ITEMS = ["APPLE", "BANANA", "MELON", "LIME"]

# Time ranges for each item (24-hour format)
TIME_PREFERENCES = {
    "APPLE": (6, 12),    # Morning: 6 AM - 12 PM
    "BANANA": (9, 15),   # Mid-morning to early afternoon: 9 AM - 3 PM
    "MELON": (12, 18),   # Afternoon: 12 PM - 6 PM
    "LIME": (18, 23)     # Evening: 6 PM - 11 PM
}

# Function to generate a random timestamp within a given hour range on a specific date
def random_timestamp(date, hour_start, hour_end):
    hour = random.randint(hour_start, hour_end - 1)
    minute = random.randint(0, 59)
    second = random.randint(0, 59)
    millisecond = random.randint(0, 999)
    return date.replace(hour=hour, minute=minute, second=second, microsecond=millisecond * 1000)

# Function to check if a date is a weekend (Saturday or Sunday)
def is_weekend(date):
    return date.weekday() >= 5  # 5 = Saturday, 6 = Sunday

# Function to append a purchase to the AOF file
def append_purchase(item, timestamp):
    with open(AOF_FILE, "a") as f:
        formatted_timestamp = timestamp.strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]  # Milliseconds with 3 digits
        f.write(f"{item} {formatted_timestamp}\n")

# Simulate purchases over a date range
def simulate_purchases(start_date, end_date):
    current_date = start_date
    while current_date <= end_date:
        # Base number of purchases per day
        base_purchases = random.randint(2, 5)
        
        # Increase Apple purchases on weekends
        apple_multiplier = 3 if is_weekend(current_date) else 1
        
        # Generate purchases
        for _ in range(base_purchases):
            # Choose item based on time preference with weighted Apple increase on weekends
            if is_weekend(current_date) and random.random() < 0.7:  # 70% chance of Apple on weekends
                item = "APPLE"
                timestamp = random_timestamp(current_date, TIME_PREFERENCES["APPLE"][0], TIME_PREFERENCES["APPLE"][1])
            else:
                item = random.choice(ITEMS)
                time_range = TIME_PREFERENCES[item]
                timestamp = random_timestamp(current_date, time_range[0], time_range[1])
            
            append_purchase(item, timestamp)
        
        # Add extra Apple purchases on weekends
        if is_weekend(current_date):
            for _ in range(random.randint(1, apple_multiplier)):
                timestamp = random_timestamp(current_date, TIME_PREFERENCES["APPLE"][0], TIME_PREFERENCES["APPLE"][1])
                append_purchase("APPLE", timestamp)
        
        current_date += timedelta(days=1)

# Main execution with command-line arguments
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Simulate shopping cart purchases and append to AOF file.")
    parser.add_argument("--start", required=True, help="Start date in YYYY-MM-DD format (e.g., 2025-03-23)")
    parser.add_argument("--end", required=True, help="End date in YYYY-MM-DD format (e.g., 2025-03-29)")
    
    args = parser.parse_args()
    
    try:
        start_date = datetime.strptime(args.start, "%Y-%m-%d")
        end_date = datetime.strptime(args.end, "%Y-%m-%d")
        
        if end_date < start_date:
            raise ValueError("End date must be on or after start date")
        
        simulate_purchases(start_date, end_date)
        print(f"Simulated purchases from {args.start} to {args.end} appended to {AOF_FILE}")
        
    except ValueError as e:
        print(f"Error: {e}. Please provide valid dates in YYYY-MM-DD format.")