#!/usr/bin/env python3

'''
used to generate plots for the report

reads tsv files from the output directory and generates plots
'''

import os
import statistics
import numpy as np
import matplotlib.pyplot as plt

alg = 'beecolony_tsp'  # reference or firelfy

out_path = 'outputs/'
files = os.listdir(out_path)

errors_iter = dict()
avg_evals = 0
avg_runtime = 0
num_files = 0
for file in files:
	if alg in file and '.tsv' in file:
		with open(out_path + file) as f:
			content = f.readlines()
			content = content[1:]
			for line in content:
				line = line.split('\t')
				error = float(line[-1].strip())
				key = int(line[0])
				errors = errors_iter.get(key)
				if errors == None:
					errors = []
				errors.append(error)
				errors_iter[key] = errors
				if line[0] == content[-1].split('\t')[0]:
					avg_evals += int(line[1])
					avg_runtime += float(line[2])
		num_files += 1

avg_evals = int(avg_evals / num_files)
avg_runtime = avg_runtime / num_files

# calculate average error over runs
averages = []
std_deviations = []
sorted_keys = list(sorted(errors_iter.keys()))
for key in sorted_keys:
	averages.append(sum(errors_iter[key])/len(errors_iter[key]))
	std_deviations.append(statistics.pstdev(errors_iter[key]))

print('error:',  averages[-1], '+/-', std_deviations[-1])
print(avg_evals, 'evaluations on average')
print('took', avg_runtime, 's on average')

# plot
plt.scatter(errors_iter.keys(), averages)
plt.errorbar(errors_iter.keys(), averages, yerr=std_deviations)	
plt.plot(errors_iter.keys(), averages)
plt.xlabel('iterations')
plt.ylabel('error')
x1, x2, y1, y2 = plt.axis()  
plt.axis((x1, x2, 25000, 50000))
plt.grid()
plt.savefig(out_path + alg + '.png', dpi=300)
