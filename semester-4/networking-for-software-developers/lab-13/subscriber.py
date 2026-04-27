import json
import paho.mqtt.client as mqtt
from util import print_data

BROKER = "broker.hivemq.com"
PORT = 1883
TOPIC = "julian/mqtt/homework/demo"

def on_message(client, userdata, msg):
    decoded_message = msg.payload.decode("utf-8")
    data = json.loads(decoded_message)
    print_data(data)

client = mqtt.Client(callback_api_version=mqtt.CallbackAPIVersion.VERSION2)
client.on_message = on_message

client.connect(BROKER, PORT, 60)
client.subscribe(TOPIC)

print("Subscriber connected and waiting for messages...")
client.loop_forever()