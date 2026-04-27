import random
import time
import paho.mqtt.client as mqtt

class SubscriberClient:
    """
    MQTT network class for subscribing.
    """

    def __init__(self, broker="localhost", port=1883, topic="julian/humidity"):
        self._broker = broker
        self._port = port
        self._topic = topic
        self._connected = False
        self._client = None
        self._message_callback = None
        self._client_id = self._make_client_id()

    def _make_client_id(self):
        return f"sub-{int(time.time() * 1000)}-{random.randint(1000, 9999)}"

    def set_message_callback(self, callback):
        self._message_callback = callback

    def _on_connect(self, client, userdata, flags, reason_code, properties=None):
        self._connected = (reason_code == 0)
        if self._connected:
            client.subscribe(self._topic)

    def _on_disconnect(self, client, userdata, disconnect_flags, reason_code, properties=None):
        self._connected = False

    def _on_message(self, client, userdata, msg):
        decoded = msg.payload.decode("utf-8")
        if self._message_callback is not None:
            self._message_callback(decoded)

    def connect(self):
        if self._client is not None:
            return self._connected

        self._client = mqtt.Client(
            client_id=self._client_id,
            protocol=mqtt.MQTTv311,
            callback_api_version=mqtt.CallbackAPIVersion.VERSION2
        )
        self._client.on_connect = self._on_connect
        self._client.on_disconnect = self._on_disconnect
        self._client.on_message = self._on_message

        self._client.connect(self._broker, self._port, 60)
        self._client.loop_start()

        time.sleep(0.2)
        return self._connected

    def disconnect(self):
        if self._client is not None:
            try:
                self._client.loop_stop()
                self._client.disconnect()
            finally:
                self._client = None
                self._connected = False

    @property
    def connected(self):
        return self._connected