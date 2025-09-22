def calculate_volume(height: int, area: int) -> int:
    volume: int = area * height
    return volume

box_length = 5
box_width = 10
box_height = 3

rectangle_area = box_length * box_width

volume = calculate_volume(rectangle_area, box_height)
print(volume)
