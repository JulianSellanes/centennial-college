import random
import time
from julian_data_generator import SensorDataGenerator

# Julian Sellanes (301494667)

start_id = 111

sensor = SensorDataGenerator(
    minimum=58.0,
    maximum=90.0,
    baseline=0.50,
    spread=0.18,
    min_segment=28,
    max_segment=42,
    drift_factor=0.08,
    wiggle_step=0.007,
    wiggle_limit=0.03,
    jitter=0.002
)

player_names = ["Nova", "Blaze", "Shadow", "Pixel", "Viper"]
ranks = ["Bronze", "Silver", "Gold", "Platinum"]
statuses = ["Alive", "Respawning", "In Combat", "Exploring"]
maps = ["Ice Arena", "Desert Ruins", "Cyber City", "Forest Base"]
weapons = ["Sword", "Blaster", "Sniper", "Bow"]

def create_data():
    global start_id
    start_id += 1

    humidity = round(sensor.reading, 1)

    data = {
        "id": start_id,
        "player": {
            "username": random.choice(player_names),
            "rank": random.choice(ranks),
            "status": random.choice(statuses)
        },
        "match": {
            "map": random.choice(maps),
            "weapon": random.choice(weapons),
            "score": random.randint(0, 50),
            "level": random.randint(1, 20)
        },
        "time": time.asctime(),
        "environment": {
            "humidity": humidity
        }
    }

    return data

def print_data(data):
    print("------- Message Received -------")
    print(f"ID: {data['id']}")
    print(f"Time: {data['time']}")
    print(f"Player: {data['player']['username']}")
    print(f"Rank: {data['player']['rank']}")
    print(f"Status: {data['player']['status']}")
    print(f"Map: {data['match']['map']}")
    print(f"Weapon: {data['match']['weapon']}")
    print(f"Score: {data['match']['score']}")
    print(f"Level: {data['match']['level']}")
    print(f"Humidity: {data['environment']['humidity']} %")
    print("--------------------------------")
    print("")