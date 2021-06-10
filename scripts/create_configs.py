#!/usr/bin/env python3

import xml.dom.minidom as md
import random
from pathlib import Path

SCRIPT_DIR = Path(__file__).parent
TEMPLATE = SCRIPT_DIR/"template.xml"
CONFIG_DIR = Path("configs/generated")
LOG_DIR = Path("log")
NUMBER_CONFIGS = 100

range_populations = range(20, 160, 20)
range_alpha = [0, 0.001, 0.005, 0.01, 0.05, 0.1, 0.5, 1, 2, 5, 10]
range_beta0 = [0, 0.001, 0.005, 0.01, 0.05, 0.1, 0.5, 1, 2, 5, 10]
range_gamma = [0, 0.001, 0.005, 0.01, 0.05, 0.1, 0.5, 1, 2, 5, 10]

file = md.parse(str(TEMPLATE))

CONFIG_DIR.mkdir(parents=True, exist_ok=True)

for i in range(NUMBER_CONFIGS):
  for prop in file.getElementsByTagName("property"):
    if prop.getAttribute("name") == "populationSize":
      prop.childNodes[0].nodeValue = range_populations[random.randint(0, len(range_populations)-1)]

    if prop.getAttribute("name") == "alpha":
      if prop.childNodes[0].data == "0.0":
        prop.childNodes[0].nodeValue = range_alpha[random.randint(0, len(range_alpha)-1)]

    if prop.getAttribute("name") == "beta0":
      prop.childNodes[0].nodeValue = range_beta0[random.randint(0, len(range_beta0)-1)]

    if prop.getAttribute("name") == "gamma":
      prop.childNodes[0].nodeValue = range_gamma[random.randint(0, len(range_gamma)-1)]

    if prop.getAttribute("name") == "filename":
      prop.childNodes[0].nodeValue = LOG_DIR/f'log_{i}.tsv'

  with open(CONFIG_DIR/f'config_{i}.xml', "w") as fs:
    fs.write(file.toxml())
    fs.close()
