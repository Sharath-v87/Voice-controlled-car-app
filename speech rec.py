import speech_recognition as sr
def audio_rec():
    r=sr.Recognizer()
    mic = sr.Microphone()
    with mic as source:
        r.adjust_for_ambient_noise(source)
        audio = r.listen(source)
    l=r.recognize_google(audio)
    if l == "move front" or l == "move forward":
        return 1
    elif l == "move back" or l == "move backward":
        return 2
    elif l == "move right" :
        return 3
    elif l == "move left":
        return 4
    else:
        return 5
# print("start talking")
# ip=audio_rec()
# print(ip)
# test=sr.AudioFile('audio_files_harvard.wav')
# with test as source:
#     r.adjust_for_ambient_noise(source)
#     audio = r.record(source)
