#!/bin/bash
if [ ! -f "$1" ]; then
	echo "Input file '$1' does not exist"
	exit
elif [ -z "$2" ]; then
	echo "No output file was specified"
	exit
fi

stripped_file="${2//.*/}"

blender -b -P sprite_batcher.py -- "$1" "$stripped_file"

(cd blender-output
convert -loop 0 -delay 10 -alpha off "$stripped_file[0-9]*".png "$stripped_file.gif"
montage "$stripped_file[0-9]*.png" -tile 4x2 -geometry 512x512+0+0 -background transparent "$2"
)
