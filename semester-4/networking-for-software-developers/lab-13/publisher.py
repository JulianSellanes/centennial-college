import json
import time
import paho.mqtt.client as mqtt
from util import create_data

BROKER = "broker.hivemq.com"
PORT = 1883
TOPIC = "julian/mqtt/homework/demo"

client = mqtt.Client(callback_api_version=mqtt.CallbackAPIVersion.VERSION2)

client.connect(BROKER, PORT, 60)
client.loop_start()

print("Publisher connected.")

for i in range(10):
    payload_dict = create_data()
    payload_string = json.dumps(payload_dict)

    client.publish(TOPIC, payload_string)
    print(f"Published message {i + 1}: {payload_string}")
    print("")

    time.sleep(1)

client.loop_stop()
client.disconnect()
print("Publisher disconnected.")