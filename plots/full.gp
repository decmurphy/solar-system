set key off
set term png
set size ratio -1
set output 'images/full.png'
p 'output/Sun.dat' u 1:3 w l, 'output/Mercury.dat' u 1:3 w l, 'output/Venus.dat' u 1:3 w l, 'output/Earth.dat' u 1:3 w l, 'output/Moon.dat' u 1:3 w l, 'output/Mars.dat' u 1:3 w l, 'output/Jupiter.dat' u 1:3 w l, 'output/Saturn.dat' u 1:3 w l, 'output/Uranus.dat' u 1:3 w l, 'output/Neptune.dat' u 1:3 w l, 'output/Pluto.dat' u 1:3 w l
