set key off
#set term png
#set output 'images/inner.png'
set view equal xyz
splot 'output/Sun.dat' u 1:2:3 w l, 'output/Mercury.dat' u 1:2:3 w l, 'output/Venus.dat' u 1:2:3 w l, 'output/Earth.dat' u 1:2:3 w l, 'output/Luna.dat' u 1:2:3 w l, 'output/Mars.dat' u 1:2:3 w l
