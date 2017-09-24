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
convert -delay 20 -alpha off "$stripped_file[0-9]*".png "$stripped_file.gif"
montage "$stripped_file[0-9]*.png" -tile 4x2 -geometry 512x512+0+0 -background transparent "$2"
mv "${stripped_file}000.png" ${stripped_file}_S.png
mv "${stripped_file}001.png" ${stripped_file}_SE.png
mv "${stripped_file}002.png" ${stripped_file}_E.png
mv "${stripped_file}003.png" ${stripped_file}_NE.png
mv "${stripped_file}004.png" ${stripped_file}_N.png
mv "${stripped_file}005.png" ${stripped_file}_NW.png
mv "${stripped_file}006.png" ${stripped_file}_W.png
mv "${stripped_file}007.png" ${stripped_file}_SW.png
)
