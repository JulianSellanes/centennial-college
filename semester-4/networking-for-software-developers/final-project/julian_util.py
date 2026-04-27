import json
import time

class HumidityPacketFactory:
    """
    Handles packet ids, packet creation, JSON conversion, and human-readable formatting.
    """
    
    def __init__(self, start_id=111, location="Toronto Campus Outdoors in Winter"):
        self._current_id = start_id
        self._location = location

    def reserve_packet_id(self):
        self._current_id += 1
        return self._current_id

    def build_packet(self, packet_id, humidity_value, publisher_name, is_corrupt=False):
        return {
            "packet_id": packet_id,
            "timestamp": time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()),
            "publisher": publisher_name,
            "location": self._location,
            "humidity": round(humidity_value, 1),
            "unit": "%",
            "status": "wild" if is_corrupt else "normal"
        }

    def to_json(self, packet_dict):
        return json.dumps(packet_dict)

    def from_json(self, payload_string):
        return json.loads(payload_string)

    def pretty_text(self, packet_dict):
        return (
            f"Packet ID: {packet_dict['packet_id']}\n"
            f"Time: {packet_dict['timestamp']}\n"
            f"Publisher: {packet_dict['publisher']}\n"
            f"Location: {packet_dict['location']}\n"
            f"Humidity: {packet_dict['humidity']} {packet_dict['unit']}\n"
            f"Status: {packet_dict['status']}"
        )