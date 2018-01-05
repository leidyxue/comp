# coding:utf-8

import re

req_map = {}

big_time = 0
sum_time = 0
sum_num = 0

for line in open("/mnt/share_windows/test.txt", "r").readlines():
    match = re.match("^.*Start time:(\d*).*method:(\w*), url:(.*), body:(.*)$", line)
    if match:
        key = match.group(1)
        req_map[key] = match
        continue
    match = re.match("^.*start time:(\d*), status code:.* cost time: *(\d*) .*$", line)
    if match:
        key = match.group(1)
        cost = int(match.group(2))
        sum_num += 1
        sum_time += cost
        if cost > 500:
            req = req_map[key]
            print cost, key, req.group(2), req.group(3), req.group(4)
            big_time += 1
        if key in req_map:
            del req_map[key]

print big_time, sum_time / sum_num
