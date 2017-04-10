#!user/bin/env python
# -*- coding: utf-8 -*-

# get current path
# find dir: build/outputs/apk/dday-collect-debug.apk 正则
# copy and build
import os
from os.path import dirname, realpath

import re

import shutil

current_dir = dirname(realpath(__file__))
# print(current_dir)
parent_dir = current_dir + "/build/outputs/apk/"
dst_dir = 'C:/Users/justi/Desktop/Version_debug/'
print parent_dir
listdir = os.listdir(parent_dir)
print listdir
pattern = re.compile(r'\S+-debug.apk')
for file in listdir:
    match = pattern.match(file)
    if match:
        apk = match.group()
        # print apk
        apk_dir = parent_dir+apk
        print apk_dir
        shutil.copy(apk_dir, dst_dir)
        #bat
        os.chdir(dst_dir)
        os.system(dst_dir+"bat_push-all.bat")




