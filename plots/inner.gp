set key off
set term png
set output 'images/inner.png'
p 'output/Sun.dat' u 1:3 w l, 'output/Mercury.dat' u 1:3 w l, 'output/Venus.dat' u 1:3 w l, 'output/Earth.dat' u 1:3 w l, 'output/Moon.dat' u 1:3 w l, 'output/Mars.dat' u 1:3 w l
