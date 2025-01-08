# Android Pathfinder Panel Client

This project was created to display Telos Pathfinder Pro panels on [Iiyama Android Tablets](https://iiyama.com/gl_en/products/prolite-tw1025lasc-b1pnr/) and the [AliExpress](https://www.aliexpress.com/item/1005003104677581.html) (Untested, but I think it's the same hardware) equivalents.

It is tested against the linked Iiyama model and will currently not work on tablets without the LED modules.

## Disclaimer

I am not an Android developer; in fact this is the first Android app I've ever written. I don't even have an Android phone. The project probably contains many 'bad' practices, but it works for me so...

## Usage

For now the app isn't distributed on the Play Store. To use, download the APK from the releases section to the tablet (either via Chrome or a USB stick) and install from there.

Once open, there is an invisible button in the top left of the screen to open the settings. Provide the IP or hostname (untested, but should work) of your Pathfinder Pro instance, along with the username, password, panel name and the name of two memory slots used to control the Blue and Red LED surrounds (if you'd like other colours, please open an issue. I have no need for them but am not against making this more configurable.)
Once you press save, the panel should reload and you should see your panel. Entering the value 'ON' in either logic slot should cause the LEDs to light.

The red slot takes preference over the blue one, as we use that for Mic Live indications.

If the LED surround is yellow, this indicates an error connecting to pathfinder. There this isn't surfaced anywhere for now, so check your panel loads in the browser and all the info is correct.

Note that our Iiyama tablets needed a Chrome update in order to display properly.